package com.github.lzk90s.fttb.logistics.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.lzk90s.fttb.logistics.dao.entity.CityEntity;
import com.github.lzk90s.fttb.logistics.dao.mapper.CityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CityService extends ServiceImpl<CityMapper, CityEntity> {
}
