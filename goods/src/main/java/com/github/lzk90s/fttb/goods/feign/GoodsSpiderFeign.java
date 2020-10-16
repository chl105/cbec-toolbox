package com.github.lzk90s.fttb.goods.feign;

import com.github.lzk90s.fttb.goods.feign.dto.CategoryDTO;
import com.github.lzk90s.fttb.goods.feign.dto.GoodsInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "goodsSpider", url = "${SPIDER_HOST:http://spider:33023}/goods")
public interface GoodsSpiderFeign {
    @GetMapping("/list_all_category")
    List<CategoryDTO> listAllCategory();

    @GetMapping("/list_category_goods")
    List<GoodsInfoDTO> listCategoryGoods(@RequestParam("category") String category, @RequestParam("sort") String sort);

    @GetMapping("/search_goods_by_image")
    List<GoodsInfoDTO> searchGoodsByImage(@RequestParam("image_url") String imageUrl, @RequestParam("max_price") Float maxPrice);
}
