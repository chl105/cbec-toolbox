package com.github.lzk90s.cbec.order.feign;

import com.github.lzk90s.cbec.internal.api.auth.PlatformAccountApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("auth")
public interface PlatformAccountApiFeign extends PlatformAccountApi {
}
