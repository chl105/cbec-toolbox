package com.github.lzk90s.cbec.logistics.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.lzk90s.cbec.logistics.dao.entity.CityEntity;
import com.github.lzk90s.cbec.logistics.dao.mapper.CityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CityService extends ServiceImpl<CityMapper, CityEntity> {
}
