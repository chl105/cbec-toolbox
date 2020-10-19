package com.github.lzk90s.fttb.goods.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.lzk90s.fttb.goods.dao.entity.GoodsEntity;
import com.github.lzk90s.fttb.goods.feign.GoodsSpiderApiFeign;
import com.github.lzk90s.fttb.goods.model.Goods;
import com.github.lzk90s.fttb.goods.service.EcommercePlatformService;
import com.github.lzk90s.fttb.goods.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@Component
@Slf4j
@RestController
@RequestMapping("/tasks/goods_spider_task")
public class GoodsSpiderTask {
    @Value("${maxGoodsNum:100}")
    private int maxGoodsNum;
    @Value("${defaultSort:most-popular}")
    private String defaultSort;

    @Autowired
    private EcommercePlatformService ecommercePlatformService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsSpiderApiFeign goodsSpiderFeign;

    @GetMapping("/execute")
    @Scheduled(cron = "0 0 */1 * * ?")
    public void execute() {
        log.info("Start found goods, please wait .....");

        var platformList = ecommercePlatformService.selectList(new EntityWrapper<>());
        if (CollectionUtils.isEmpty(platformList)) {
            return;
        }

        platformList.forEach(platform -> {
            Wrapper<GoodsEntity> queryWrapper = new EntityWrapper<>(new GoodsEntity());
            queryWrapper.eq("purchased", false).and().eq("platform", platform.getName());
            int count = goodsService.selectCount(queryWrapper);
            if (count > maxGoodsNum) {
                log.info("Max goods reached, ignore!");
                return;
            }

            syncPlatformGoods(platform.getName());
        });
    }

    private void syncPlatformGoods(String platformName) {
        var categoryList = goodsSpiderFeign.listAllCategory();
        if (CollectionUtils.isEmpty(categoryList)) {
            log.warn("No category found for platform {}", platformName);
            return;
        }

        categoryList.forEach(category -> {
            var goodsList = goodsSpiderFeign.listCategoryGoods(category.getName(), defaultSort);
            if (CollectionUtils.isEmpty(goodsList)) {
                return;
            }
            var goodsListDO = goodsList.stream()
                    .map(s -> Goods.getEntityConverter().doForward(s))
                    .peek(s -> s.setPlatform(platformName))
                    .peek(s -> s.setCategory(category.getName()))
                    .collect(Collectors.toList());
            goodsService.insertOrUpdateBatch(goodsListDO);
            log.info("Found {} goods in category {}", goodsListDO.size(), category.getName());
        });
    }
}
