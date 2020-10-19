package com.github.lzk90s.fttb.auth.controller.internal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.lzk90s.fttb.auth.dao.entity.UserEntity;
import com.github.lzk90s.fttb.auth.service.UserService;
import com.github.lzk90s.fttb.internal.api.auth.UserApi;
import com.github.lzk90s.fttb.internal.api.auth.UserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/internal/user")
public class UserApiImpl implements UserApi {
    @Autowired
    private UserService userService;

    @Override
    public UserInfoDTO getUserInfo(String userName) {
        return Optional.ofNullable(userService.selectOne(new EntityWrapper<UserEntity>().eq("name", userName)))
                .map(s -> UserEntity.getConverter().doForward(s)).orElseThrow(IllegalArgumentException::new);
    }
}
