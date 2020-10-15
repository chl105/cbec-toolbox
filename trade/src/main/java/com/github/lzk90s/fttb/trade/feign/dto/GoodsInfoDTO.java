package com.github.lzk90s.fttb.trade.feign.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.lzk90s.fttb.trade.model.Goods;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class GoodsInfoDTO {
    private String id;
    private String subject;
    @JsonProperty("detail_url")
    private String detailUrl;
    @JsonProperty("image_url")
    private String imageUrl;
    private Float price;
    @JsonProperty("seller_name")
    private String sellerName;
    @JsonProperty("seller_shop_url")
    private String sellerShopUrl;
    @JsonProperty("sell_count")
    private String sellCount;

    public static ConverterImpl getConverter(){
        return new ConverterImpl();
    }

    public static class ConverterImpl extends Converter<GoodsInfoDTO, Goods>{

        @Override
        public Goods doForward(GoodsInfoDTO goodsInfoDTO) {
            Goods goods = new Goods();
            BeanUtils.copyProperties(goodsInfoDTO, goods);
            return goods;
        }

        @Override
        public GoodsInfoDTO doBackward(Goods goods) {
            throw new UnsupportedOperationException();
        }
    }
}
