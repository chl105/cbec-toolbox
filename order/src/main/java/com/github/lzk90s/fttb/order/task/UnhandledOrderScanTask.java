package com.github.lzk90s.fttb.order.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.lzk90s.fttb.common.util.JsonUtil;
import com.github.lzk90s.fttb.common.util.UserUtil;
import com.github.lzk90s.fttb.internal.api.spider.OrderDTO;
import com.github.lzk90s.fttb.order.dao.entity.OrderEntity;
import com.github.lzk90s.fttb.order.feign.MessageApiFeign;
import com.github.lzk90s.fttb.order.feign.OrderSpiderApiFeign;
import com.github.lzk90s.fttb.order.feign.PlatformAccountApiFeign;
import com.github.lzk90s.fttb.order.feign.UserApiFeign;
import com.github.lzk90s.fttb.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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

    @Scheduled(cron = "0 0 1/* ? ? ?")
    public void scan() {
        var accountList = platformAccountApiFeign.listPlatformAccountByUser(UserUtil.getUserName());
        if (CollectionUtils.isEmpty(accountList)) {
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
                    .collect(Collectors.toList());
            orderService.insertOrUpdateBatch(orderEntityList);

            var userInfo = userApiFeign.getUserInfo(account.getUserName());
            messageApiFeign.sendMail(userInfo.getEmail(), buildNotifyMessage(orderList));
        });
    }

    private String buildNotifyMessage(List<OrderDTO> orderDTOList) {
        StringBuilder sb = new StringBuilder();
        sb.append("你有").append(String.valueOf(orderDTOList.size())).append("个未处理订单，请及时处理！");
        orderDTOList.forEach(s -> sb.append(JsonUtil.obj2json(s)).append("\n"));
        return sb.toString();
    }
}
