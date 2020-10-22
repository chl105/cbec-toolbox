package com.github.lzk90s.cbec.goods.model;

import com.github.lzk90s.cbec.internal.api.spider.GoodsInfoDTO;
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


}
