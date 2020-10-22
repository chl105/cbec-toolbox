package com.github.lzk90s.cbec.order.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.lzk90s.cbec.internal.api.auth.PlatformAccountDTO;
import com.github.lzk90s.cbec.internal.api.messager.MessageDTO;
import com.github.lzk90s.cbec.internal.api.spider.OrderDTO;
import com.github.lzk90s.cbec.order.dao.entity.OrderEntity;
import com.github.lzk90s.cbec.order.feign.MessageApiFeign;
import com.github.lzk90s.cbec.order.feign.OrderSpiderApiFeign;
import com.github.lzk90s.cbec.order.feign.PlatformAccountApiFeign;
import com.github.lzk90s.cbec.order.feign.UserApiFeign;
import com.github.lzk90s.cbec.order.service.OrderService;
import com.github.lzk90s.cbec.order.service.UnhandledOrderNotifyService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/tasks/unhandled_order_scan_task")
@Component
public class UnhandledOrderScanTask {
    @Autowired
    private UnhandledOrderNotifyService unhandledOrderNotifyService;

    @GetMapping("/execute")
    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void scan() {
        unhandledOrderNotifyService.scanOrder();
    }
}
