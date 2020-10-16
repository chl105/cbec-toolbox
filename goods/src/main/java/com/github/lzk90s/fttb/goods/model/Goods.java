package com.github.lzk90s.fttb.goods.model;

import lombok.Data;

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
}
