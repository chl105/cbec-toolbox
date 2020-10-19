package com.github.lzk90s.fttb.order.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.github.lzk90s.fttb.internal.api.spider.OrderDTO;
import com.google.common.base.Converter;

import java.util.Date;

@TableName("t_order")
public class OrderEntity {
    @TableId
    private String id;
    private String platform;
    private String type;
    private Date confirmTime;
    private String sn;
    private Integer num;
    private Float price;
    private Date lastNotifyTime;

    public static ConverterImpl getConverter() {
        return new ConverterImpl();
    }

    public static class ConverterImpl extends Converter<OrderEntity, OrderDTO> {

        @Override
        public OrderDTO doForward(OrderEntity orderEntity) {
            return null;
        }

        @Override
        public OrderEntity doBackward(OrderDTO orderDTO) {
            return null;
        }
    }
}
