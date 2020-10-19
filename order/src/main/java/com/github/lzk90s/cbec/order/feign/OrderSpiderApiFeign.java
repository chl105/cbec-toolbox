package com.github.lzk90s.cbec.order.feign;

import com.github.lzk90s.cbec.internal.api.spider.OrderSpiderApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "orderSpider", url = "${SPIDER_HOST:http://spider:33023}/order")
public interface OrderSpiderApiFeign extends OrderSpiderApi {
}
