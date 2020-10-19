package com.github.lzk90s.fttb.order.feign;

import com.github.lzk90s.fttb.internal.api.messager.MessageApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("messager")
public interface MessageApiFeign extends MessageApi {
}
