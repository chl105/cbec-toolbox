package com.github.lzk90s.cbec.internal.api.spider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
    private String platform;
    private String category;
}
