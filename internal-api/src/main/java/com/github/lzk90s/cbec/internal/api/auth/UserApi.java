package com.github.lzk90s.cbec.internal.api.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/internal/user")
public interface UserApi {
    @GetMapping("/info")
    UserInfoDTO getUserInfo(@RequestParam("userName") String userName);
}
