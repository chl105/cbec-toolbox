package com.github.lzk90s.cbec.goods.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.lzk90s.cbec.common.util.JsonUtil;
import com.github.lzk90s.cbec.goods.dao.entity.GoodsEntity;
import com.github.lzk90s.cbec.goods.dao.entity.GoodsSupplierEntity;
import com.github.lzk90s.cbec.goods.feign.GoodsSpiderApiFeign;
import com.github.lzk90s.cbec.internal.api.spider.GoodsInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
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

        platformList.forEach(platform -> {
            Wrapper<GoodsEntity> queryWrapper = new EntityWrapper<>(new GoodsEntity());
            queryWrapper.eq("purchased", false).and().eq("platform", platform.getName());
            int count = goodsService.selectCount(queryWrapper);
            if (count > maxGoodsNum) {
                log.info("Max goods reached, ignore!");
                return;
            }

            grabPlatformGoods(platform.getName());
        });
    }

    private void grabPlatformGoods(String platformName) {
        var categoryList = goodsSpiderFeign.listAllCategory(platformName);
        if (CollectionUtils.isEmpty(categoryList)) {
            log.warn("No category found for platform {}", platformName);
            return;
        }

        categoryList.forEach(category -> {
            var goodsList = goodsSpiderFeign.listCategoryGoods(platformName, category.getName(), defaultSort);
            if (CollectionUtils.isEmpty(goodsList)) {
                return;
            }

            var goodsListDO = goodsList.stream()
                    .map(s -> GoodsEntity.getConverter().doForward(s))
                    .collect(Collectors.toList());
            goodsService.insertOrUpdateBatch(goodsListDO);
            log.info("Found {} goods in category {}", goodsListDO.size(), category.getName());

            // 同步商品供应商
            goodsListDO.forEach(goods -> grabSupplier4Goods(goods.getId(), goods.getImageUrl(), goods.getPrice()));
        });
    }

    private void grabSupplier4Goods(String goodsId, String goodsImageUrl, float goodsPriceUSD) {
        float maxPrice = calculateBuyPriceCNY(goodsPriceUSD);
        var goodsList = goodsSpiderFeign.searchGoodsByImage(goodsImageUrl, maxPrice);
        if (CollectionUtils.isEmpty(goodsList)) {
            return;
        }

        var supplierGoods = calculateBestSupplier(goodsList);
        if (null == supplierGoods) {
            log.warn("No suitable supplier for goods {}", goodsId);
            return;
        }

        log.info("Goods = {}, priceUSD = {}, buyPriceCNY = {}, supplier = {}",
                goodsId, goodsPriceUSD, maxPrice, JsonUtil.obj2json(supplierGoods));

        goodsSupplierService.insertOrUpdate(GoodsSupplierEntity.getConverter().doBackward(supplierGoods, goodsId));
    }

    private float calculateBuyPriceCNY(float goodsPriceUSD) {
        float usd = (goodsPriceUSD > logisticsFeeUSD) ? goodsPriceUSD - logisticsFeeUSD : goodsPriceUSD;
        return (usd * cny2UsdExchangeRate) / buy2SellPriceRate;
    }

    private GoodsInfoDTO calculateBestSupplier(List<GoodsInfoDTO> goodsSupplierList) {
        if (goodsSupplierList.isEmpty()) {
            return null;
        } else if (goodsSupplierList.size() == 1) {
            return goodsSupplierList.get(0);
        } else {
            return goodsSupplierList.get(goodsSupplierList.size() / 2);
        }
    }
}
