package com.github.lzk90s.fttb.auth.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.lzk90s.fttb.auth.dao.entity.UserEntity;
import com.github.lzk90s.fttb.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

/**
 * @author liuzhikun
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.selectOne(new EntityWrapper<>(new UserEntity().setName(username).setState(true)));
        String password = Optional.ofNullable(userEntity)
                .map(UserEntity::getPassword).orElseThrow(() -> new BizException("用户不存在"));
        return new User(username, password, userEntity.getState(),
                true, true, true,
                new HashSet<>());
    }
}
