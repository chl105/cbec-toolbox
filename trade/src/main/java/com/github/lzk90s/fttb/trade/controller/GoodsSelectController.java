package com.github.lzk90s.fttb.trade.controller;

import com.github.lzk90s.fttb.common.rest.Result;
import com.github.lzk90s.fttb.trade.feign.GoodsSpiderFeign;
import com.github.lzk90s.fttb.trade.feign.dto.CategoryDTO;
import com.github.lzk90s.fttb.trade.feign.dto.GoodsInfoDTO;
import com.github.lzk90s.fttb.trade.model.Category;
import com.github.lzk90s.fttb.trade.model.Goods;
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
    private GoodsSpiderFeign goodsSpiderFeign;

    @GetMapping("/list_all_category")
    public Result<List<Category>> listAllCategory() {
        var list = goodsSpiderFeign.listAllCategory();
        if (CollectionUtils.isEmpty(list)){
            return Result.ok();
        }
        var result = list.stream()
                .map(s->CategoryDTO.getConverter().doForward(s))
                .collect(Collectors.toList());
        return Result.ok(result);
    }

    @GetMapping("/list_category_goods")
    public Result<List<Goods>> listCategoryGoods(@NotBlank @RequestParam String category, @RequestParam String sort) {
        var list = goodsSpiderFeign.listCategoryGoods(category, sort);
        if (CollectionUtils.isEmpty(list)){
            return Result.ok();
        }
        var result = list.stream()
                .map(s->GoodsInfoDTO.getConverter().doForward(s))
                .collect(Collectors.toList());
        return Result.ok(result);
    }

    @GetMapping("/search_goods_by_image")
    public Result<List<Goods>> searchGoodsByImage(@RequestParam String imageUrl, @RequestParam Float maxPrice){
        var list = goodsSpiderFeign.searchGoodsByImage(imageUrl, maxPrice);
        if (CollectionUtils.isEmpty(list)){
            return Result.ok();
        }
        var result = list.stream()
                .map(s->GoodsInfoDTO.getConverter().doForward(s))
                .collect(Collectors.toList());
        return Result.ok(result);
    }
}
