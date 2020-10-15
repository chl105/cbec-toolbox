package com.github.lzk90s.fttb.trade.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "orderSpider", url = "${SPIDER_HOST:http://spider:33023}/order")
public interface OrderSpiderFeign {
}
