package com.github.lzk90s.cbec.goods.dao.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("t_ecommerce_platform")
public class EcommercePlatformEntity {
    private String name;
    private String description;
}
