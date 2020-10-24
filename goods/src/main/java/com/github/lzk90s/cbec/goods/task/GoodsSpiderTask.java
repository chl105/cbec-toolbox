package com.github.lzk90s.cbec.goods.task;

import com.github.lzk90s.cbec.goods.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@RequestMapping("/tasks/goods_spider_task")
public class GoodsSpiderTask {
    @Autowired
    private SpiderService spiderService;

    @GetMapping("/execute")
    @Scheduled(fixedDelay = 30*60*1000)
    public void execute() {
        spiderService.grabGoods();
    }
}
