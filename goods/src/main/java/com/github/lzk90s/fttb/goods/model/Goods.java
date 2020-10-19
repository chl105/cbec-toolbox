package com.github.lzk90s.fttb.goods.model;

import com.github.lzk90s.fttb.goods.dao.entity.GoodsEntity;
import com.github.lzk90s.fttb.internal.api.spider.GoodsInfoDTO;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class Goods {
    private String id;
    private String subject;
    private String detailUrl;
    private String imageUrl;
    private Float price;
    private String sellerName;
    private String sellerShopUrl;
    private String sellCount;


    public static ConverterImpl getConverter() {
        return new ConverterImpl();
    }

    public static ConverterImpl1 getEntityConverter() {
        return new ConverterImpl1();
    }

    public static class ConverterImpl extends Converter<Goods, GoodsInfoDTO> {

        @Override
        public Goods doBackward(GoodsInfoDTO goodsInfoDTO) {
            Goods goods = new Goods();
            BeanUtils.copyProperties(goodsInfoDTO, goods);
            return goods;
        }

        @Override
        public GoodsInfoDTO doForward(Goods goods) {
            throw new UnsupportedOperationException();
        }
    }

    public static class ConverterImpl1 extends Converter<GoodsInfoDTO, GoodsEntity> {

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
