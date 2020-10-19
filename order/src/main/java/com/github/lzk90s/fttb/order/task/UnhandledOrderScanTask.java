package com.github.lzk90s.fttb.order.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.lzk90s.fttb.internal.api.messager.MessageDTO;
import com.github.lzk90s.fttb.internal.api.spider.OrderDTO;
import com.github.lzk90s.fttb.order.dao.entity.OrderEntity;
import com.github.lzk90s.fttb.order.feign.MessageApiFeign;
import com.github.lzk90s.fttb.order.feign.OrderSpiderApiFeign;
import com.github.lzk90s.fttb.order.feign.PlatformAccountApiFeign;
import com.github.lzk90s.fttb.order.feign.UserApiFeign;
import com.github.lzk90s.fttb.order.service.OrderService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/tasks/unhandled_order_scan_task")
@Component
public class UnhandledOrderScanTask {
    @Autowired
    private PlatformAccountApiFeign platformAccountApiFeign;

    @Autowired
    private UserApiFeign userApiFeign;

    @Autowired
    private OrderSpiderApiFeign orderSpiderApiFeign;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MessageApiFeign messageApiFeign;

    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    private String templateName = "unhandled-order.html";

    @GetMapping("/execute")
    @Scheduled(cron = "0 0 */1 * * ?")
    public void scan() {
        var accountList = platformAccountApiFeign.listAllUserPlatformAccount();
        if (CollectionUtils.isEmpty(accountList)) {
            log.info("No account, skip");
            return;
        }

        accountList.forEach(account -> {
            var orderList = orderSpiderApiFeign.listUnhandledOrder(account.getPlatform(),
                    account.getUser(), account.getPassword());
            if (CollectionUtils.isEmpty(orderList)) {
                return;
            }

            // 删除不存在的订单
            var orderIdList = orderList.stream().map(OrderDTO::getId).collect(Collectors.toList());
            orderService.delete(new EntityWrapper<OrderEntity>().notIn("id", orderIdList));

            var orderEntityList = orderList.stream()
                    .map(s -> OrderEntity.getConverter().doBackward(s))
                    .peek(s -> s.setPlatform(account.getPlatform()))
                    .peek(s -> s.setUser(account.getUserName()))
                    .collect(Collectors.toList());
            orderService.insertOrUpdateBatch(orderEntityList);

            log.info("发现用户{}的{}个未处理订单", account.getUserName(), orderEntityList.size());
        });
    }

    @GetMapping("/notify_order")
    @Scheduled(cron = "0 5 */1 * * ?")
    public void notifyOrder() {
        var accountList = platformAccountApiFeign.listAllUserPlatformAccount();
        if (CollectionUtils.isEmpty(accountList)) {
            log.info("No account, skip");
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 1);
        Date time = calendar.getTime();

        log.info("Time = {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));

        accountList.forEach(account -> {
            // 查询没有通知过，或者已经过期的订单
            var orderList = orderService.selectList(new EntityWrapper<OrderEntity>()
                    .eq("user", account.getUserName()).le("last_notify_time", time)
                    .or().isNull("last_notify_time"));
            if (CollectionUtils.isEmpty(orderList)) {
                return;
            }

            var userInfo = userApiFeign.getUserInfo(account.getUserName());
            var messageDTO = new MessageDTO(userInfo.getEmail(), buildNotifyMessage(orderList));
            messageApiFeign.sendMail(messageDTO);

            log.info("通知用户{}有{}个未处理的订单", account.getUserName(), orderList.size());

            // 更新最后通知时间
            var newOrderList = orderList.stream()
                    .peek(s -> s.setLastNotifyTime(new Date()))
                    .collect(Collectors.toList());
            orderService.updateBatchById(newOrderList);
        });
    }

    private String buildNotifyMessage(List<OrderEntity> orderDTOList) {
        String title = "你有" + orderDTOList.size() + "个未处理订单，请及时处理！";
        Map<String, Object> model = new HashMap<>();
        model.put("title", title);
        model.put("orders", orderDTOList);

        try {
            var template = freeMarkerConfig.getConfiguration().getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
