package com.github.lzk90s.cbec.auth.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.lzk90s.cbec.auth.dao.entity.UserEntity;
import com.github.lzk90s.cbec.auth.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService extends ServiceImpl<UserMapper, UserEntity> {
}
