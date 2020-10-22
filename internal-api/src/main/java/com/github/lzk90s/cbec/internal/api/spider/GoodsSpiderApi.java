package com.github.lzk90s.cbec.internal.api.spider;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


public interface GoodsSpiderApi {
    @GetMapping("/list_all_category/{platform}")
    List<CategoryDTO> listAllCategory(@PathVariable("platform") String platform);

    @GetMapping("/list_category_goods/{platform}")
    List<GoodsInfoDTO> listCategoryGoods(@PathVariable("platform") String platform,
                                         @RequestParam("category") String category,
                                         @RequestParam("sort") String sort);

    @GetMapping("/search_goods_by_image")
    List<GoodsInfoDTO> searchGoodsByImage(@RequestParam("image_url") String imageUrl,
                                          @RequestParam("max_price") Float maxPrice);
}
