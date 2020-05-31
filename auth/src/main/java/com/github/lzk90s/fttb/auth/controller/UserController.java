package com.github.lzk90s.fttb.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


/**
 * @author wangdl
 */
@RestController
public class UserController {
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
