package com.github.lzk90s.fttb.logistics.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.lzk90s.fttb.logistics.dao.entity.CountryEntity;
import com.github.lzk90s.fttb.logistics.dao.mapper.CountryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CountryService extends ServiceImpl<CountryMapper, CountryEntity> {
}
