package com.github.lzk90s.fttb.order.feign;

import com.github.lzk90s.fttb.internal.api.auth.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("auth")
public interface UserApiFeign extends UserApi {
}
