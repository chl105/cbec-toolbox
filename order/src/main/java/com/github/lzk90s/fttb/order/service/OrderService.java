package com.github.lzk90s.fttb.order.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.lzk90s.fttb.order.dao.entity.OrderEntity;
import com.github.lzk90s.fttb.order.dao.mapper.OrderMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends ServiceImpl<OrderMapper, OrderEntity> {
}
