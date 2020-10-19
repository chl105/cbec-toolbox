package com.github.lzk90s.cbec.auth.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.lzk90s.cbec.auth.dao.entity.PlatformAccountEntity;
import com.github.lzk90s.cbec.auth.dao.mapper.PlatformAccountMapper;
import org.springframework.stereotype.Service;

@Service
public class PlatformAccountService extends ServiceImpl<PlatformAccountMapper, PlatformAccountEntity> {
}
