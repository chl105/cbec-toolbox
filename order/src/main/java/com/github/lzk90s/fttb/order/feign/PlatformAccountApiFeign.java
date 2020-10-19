package com.github.lzk90s.fttb.order.feign;

import com.github.lzk90s.fttb.internal.api.auth.PlatformAccountApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("auth")
public interface PlatformAccountApiFeign extends PlatformAccountApi {
}
