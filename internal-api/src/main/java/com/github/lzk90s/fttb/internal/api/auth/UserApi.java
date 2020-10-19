package com.github.lzk90s.fttb.internal.api.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/internal/user")
public interface UserApi {
    @GetMapping("/info")
    UserInfoDTO getUserInfo(String userName);
}
