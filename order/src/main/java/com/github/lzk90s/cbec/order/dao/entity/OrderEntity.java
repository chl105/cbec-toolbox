package com.github.lzk90s.cbec.order.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.github.lzk90s.cbec.internal.api.spider.OrderDTO;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@TableName("t_order")
public class OrderEntity {
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    private String platform;
    private String user;
    private String type;
    private Date confirmTime;
    private String sn;
    private Integer num;
    private Float price;
    private String imageUrl;
    private Date lastNotifyTime;

    public static ConverterImpl getConverter() {
        return new ConverterImpl();
    }

    public static class ConverterImpl extends Converter<OrderEntity, OrderDTO> {

        @Override
        public OrderDTO doForward(OrderEntity orderEntity) {
            throw new UnsupportedOperationException();
        }

        @Override
        public OrderEntity doBackward(OrderDTO orderDTO) {
            OrderEntity orderEntity = new OrderEntity();
            BeanUtils.copyProperties(orderDTO, orderEntity);
            return orderEntity;
        }
    }
}
