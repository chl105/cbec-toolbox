package com.github.lzk90s.cbec.order.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.lzk90s.cbec.order.dao.entity.OrderEntity;
import com.github.lzk90s.cbec.order.dao.mapper.OrderMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends ServiceImpl<OrderMapper, OrderEntity> {
}
