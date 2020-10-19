package com.github.lzk90s.cbec.order.feign;

import com.github.lzk90s.cbec.internal.api.auth.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("auth")
public interface UserApiFeign extends UserApi {
}
