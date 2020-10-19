package com.github.lzk90s.cbec.goods.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("t_goods")
public class GoodsEntity {
    @TableId
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
}
