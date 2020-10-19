package com.github.lzk90s.cbec.order.feign;

import com.github.lzk90s.cbec.internal.api.messager.MessageApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("messager")
public interface MessageApiFeign extends MessageApi {
}
