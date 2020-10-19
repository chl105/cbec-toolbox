package com.github.lzk90s.fttb.goods.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.lzk90s.fttb.common.rest.Result;
import com.github.lzk90s.fttb.goods.dao.entity.GoodsEntity;
import com.github.lzk90s.fttb.goods.feign.GoodsSpiderApiFeign;
import com.github.lzk90s.fttb.goods.model.Category;
import com.github.lzk90s.fttb.goods.model.Goods;
import com.github.lzk90s.fttb.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 选品controller
 */
@RestController
@RequestMapping("/goods")
public class GoodsSelectController {
    @Autowired
    private GoodsSpiderApiFeign goodsSpiderFeign;

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list_all_category/{platform}")
    public Result<List<Category>> listAllCategory(@PathVariable String platform) {
        var list = goodsSpiderFeign.listAllCategory();
        if (CollectionUtils.isEmpty(list)) {
            return Result.ok();
        }
        var result = list.stream()
                .map(s -> Category.getConverter().doBackward(s))
                .collect(Collectors.toList());
        return Result.ok(result);
    }

    @GetMapping("/list_category_goods")
    public Result<List<GoodsEntity>> listCategoryGoods(@NotBlank @RequestParam String category, @RequestParam String sort) {
        Wrapper<GoodsEntity> goodsEntityWrapper = new EntityWrapper<>();
        goodsEntityWrapper.eq("category", category);
        var list = goodsService.selectList(goodsEntityWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return Result.ok();
        }
        return Result.ok(list).total(goodsService.selectCount(goodsEntityWrapper));
    }

    @GetMapping("/search_goods_by_image")
    public Result<List<Goods>> searchGoodsByImage(@RequestParam String imageUrl, @RequestParam Float maxPrice) {
        var list = goodsSpiderFeign.searchGoodsByImage(imageUrl, maxPrice);
        if (CollectionUtils.isEmpty(list)) {
            return Result.ok();
        }
        var result = list.stream()
                .map(s -> Goods.getConverter().doBackward(s))
                .collect(Collectors.toList());
        return Result.ok(result);
    }
}
