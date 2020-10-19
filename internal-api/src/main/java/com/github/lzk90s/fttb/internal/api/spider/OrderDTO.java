package com.github.lzk90s.fttb.internal.api.spider;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {
    private String id;
    private String type;
    private Date confirmTime;
    private String sn;
    private Integer num;
    private Float price;
}
