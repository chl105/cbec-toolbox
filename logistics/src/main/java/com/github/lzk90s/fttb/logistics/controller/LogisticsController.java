package com.github.lzk90s.fttb.logistics.controller;

import com.github.lzk90s.fttb.common.rest.Result;
import com.github.lzk90s.fttb.logistics.model.Address;
import com.github.lzk90s.fttb.logistics.model.Label;
import com.github.lzk90s.fttb.logistics.model.Price;
import com.github.lzk90s.fttb.logistics.vendor.LogisticsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物流controller
 *
 * @author liuzhikun
 */
@RestController
@RequestMapping("/logistics")
public class LogisticsController {
    @Autowired
    private LogisticsProvider logisticsProvider;

    @GetMapping("/goods_attribute_list")
    public Result<List<Label>> getGoodsAttributeList() {
        return Result.ok(List.of(
                new Label("1", "普货"),
                new Label("2", "特货"))
        );
    }

    @GetMapping("/product_type_list")
    public Result<List<Label>> getProductTypeList() {
        return Result.ok(List.of(
                new Label("1", "挂号小包"),
                new Label("2", "平邮小包"),
                new Label("3", "E邮宝"),
                new Label("4", "专线/快递"))
        );
    }

    @GetMapping("/price")
    public Result<List<Price>> getPrice(@RequestParam String srcCountryCode,
                                        @RequestParam String srcAreaCode,
                                        @RequestParam String srcCityCode,
                                        @RequestParam String dstCountryCode,
                                        @RequestParam String dstAreaCode,
                                        @RequestParam String dstCityCode,
                                        @RequestParam String types,
                                        @RequestParam String goodsAttr,
                                        @RequestParam float weight) {
        Address src = Address.builder()
                .countryCode(srcCountryCode)
                .areaCode(srcAreaCode)
                .cityCode(srcCityCode)
                .build();

        Address dst = Address.builder()
                .countryCode(dstCountryCode)
                .areaCode(dstAreaCode)
                .cityCode(dstCityCode)
                .build();

        return Result.ok(logisticsProvider.getPrice(src, dst, types, goodsAttr, weight));
    }
}
