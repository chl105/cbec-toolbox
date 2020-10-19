package com.github.lzk90s.fttb.internal.api.spider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface GoodsSpiderApi {
    @GetMapping("/list_all_category")
    List<CategoryDTO> listAllCategory();

    @GetMapping("/list_category_goods")
    List<GoodsInfoDTO> listCategoryGoods(@RequestParam("category") String category, @RequestParam("sort") String sort);

    @GetMapping("/search_goods_by_image")
    List<GoodsInfoDTO> searchGoodsByImage(@RequestParam("image_url") String imageUrl, @RequestParam("max_price") Float maxPrice);
}
