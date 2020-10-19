package com.github.lzk90s.fttb.goods.feign;

import com.github.lzk90s.fttb.internal.api.spider.GoodsSpiderApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "goodsSpider", url = "${SPIDER_HOST:http://spider:33023}/goods")
public interface GoodsSpiderApiFeign extends GoodsSpiderApi {
}
