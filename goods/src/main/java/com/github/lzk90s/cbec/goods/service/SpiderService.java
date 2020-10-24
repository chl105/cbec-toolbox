package com.github.lzk90s.cbec.goods.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.lzk90s.cbec.common.util.JsonUtil;
import com.github.lzk90s.cbec.goods.dao.entity.GoodsEntity;
import com.github.lzk90s.cbec.goods.dao.entity.GoodsSupplierEntity;
import com.github.lzk90s.cbec.goods.feign.GoodsSpiderApiFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

@Slf4j
@Service
public class SpiderService {
    @Value("${maxGoodsNum:100}")
    private int maxGoodsNum;
    @Value("${defaultSort:most-popular}")
    private String defaultSort;
    @Value("${cny2UsdExchangeRate:7.0}")
    private float cny2UsdExchangeRate;
    @Value("${logisticsFeeUSD:3}")
    private float logisticsFeeUSD;
    @Value("${buy2SellPriceRate:3}")
    private float buy2SellPriceRate;

    @Autowired
    private EcommercePlatformService ecommercePlatformService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsSpiderApiFeign goodsSpiderFeign;
    @Autowired
    private GoodsSupplierService goodsSupplierService;

    public void grabGoods() {
        log.info("Start grab goods, please wait .....");

        var platformList = ecommercePlatformService.selectList(new EntityWrapper<>());
        if (CollectionUtils.isEmpty(platformList)) {
            return;
        }

        platformList.forEach(platform -> grabPlatformGoods(platform.getName()));
    }

    private void grabPlatformGoods(String platformName) {
        var categoryList = goodsSpiderFeign.listAllCategory(platformName);
        if (CollectionUtils.isEmpty(categoryList)) {
            log.warn("No category found for platform {}", platformName);
            return;
        }

        categoryList.forEach(category -> {
            // 如果已经达到最大数量，停止爬取
            int grabNum = getGrabNum4Category(category.getName(), platformName);
            if (grabNum <= 0) {
                log.info("Max goods reached, skip the category {}!", category.getName());
                return;
            }

            int count = 0;
            String cursor = "";
            boolean hasMoreData = true;

            // 循环爬取商品，直到达到要求的数量
            while (count < grabNum && hasMoreData) {
                var scrollResult = goodsSpiderFeign.listCategoryGoods(platformName,
                        category.getName(), defaultSort, cursor);

                // 已经爬取完了，退出
                if (CollectionUtils.isEmpty(scrollResult.getResults())) {
                    break;
                }
                // 下一页没有了，标记没有数据了
                if (StringUtils.isEmpty(scrollResult.getNextCursor())) {
                    hasMoreData = false;
                }

                cursor = scrollResult.getNextCursor();

                // 抓取商品供应商
                var goodsList = scrollResult.getResults().stream()
                        .filter(goods -> grabSupplier4Goods(goods.getId(), goods.getImageUrl(), goods.getPrice()))
                        .collect(Collectors.toList());

                if (CollectionUtils.isEmpty(goodsList)) {
                    continue;
                }

                // 保存商品信息
                var goodsEntityList = goodsList.stream()
                        .map(s -> GoodsEntity.getConverter().doForward(s))
                        .collect(Collectors.toList());
                goodsService.insertOrUpdateBatch(goodsEntityList);
                log.info("Found {} goods in category {}", goodsEntityList.size(), category.getName());

                count += goodsEntityList.size();
            }
        });
    }

    private int getGrabNum4Category(String category, String platform) {
        Wrapper<GoodsEntity> queryWrapper = new EntityWrapper<>(new GoodsEntity());
        queryWrapper.eq("category", category).and().eq("platform", platform);
        int count = goodsService.selectCount(queryWrapper);
        return maxGoodsNum - count;
    }

    private boolean grabSupplier4Goods(String goodsId, String goodsImageUrl, float goodsPriceUSD) {
        float maxPrice = calculateBuyPriceCNY(goodsPriceUSD);
        try {
            var goodsList = goodsSpiderFeign.searchGoodsByImage(goodsImageUrl, maxPrice, 3);
            if (CollectionUtils.isEmpty(goodsList)) {
                return false;
            }

            log.info("GoodsId = {}, priceUSD = {}, buyPriceCNY = {}, supplier = {}",
                    goodsId, goodsPriceUSD, maxPrice, JsonUtil.obj2json(goodsList));

            var goodsEntityList = goodsList.stream()
                    .map(s -> GoodsSupplierEntity.getConverter().doBackward(s))
                    .peek(s -> s.setGoodsId(goodsId))
                    .collect(Collectors.toList());
            goodsSupplierService.insertOrUpdateBatch(goodsEntityList);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private float calculateBuyPriceCNY(float goodsPriceUSD) {
        float usd = (goodsPriceUSD > logisticsFeeUSD) ? goodsPriceUSD - logisticsFeeUSD : goodsPriceUSD;
        return (usd * cny2UsdExchangeRate) / buy2SellPriceRate;
    }
}
