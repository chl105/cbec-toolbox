package com.github.lzk90s.cbec.goods.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.github.lzk90s.cbec.internal.api.spider.GoodsInfoDTO;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@TableName("t_goods")
public class GoodsEntity {
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    private String subject;
    private String category;
    private String platform;
    private String detailUrl;
    private String imageUrl;
    private Float price;
    private String sellerName;
    private String sellerShopUrl;
    private Boolean purchased;


    public static ConverterImpl getConverter() {
        return new ConverterImpl();
    }

    public static class ConverterImpl extends Converter<GoodsInfoDTO, GoodsEntity> {

        @Override
        public GoodsEntity doForward(GoodsInfoDTO goodsInfoDTO) {
            GoodsEntity goodsEntity = new GoodsEntity();
            BeanUtils.copyProperties(goodsInfoDTO, goodsEntity);
            return goodsEntity;
        }

        @Override
        public GoodsInfoDTO doBackward(GoodsEntity goodsEntity) {
            throw new UnsupportedOperationException();
        }
    }
}
