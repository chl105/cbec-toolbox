package com.github.lzk90s.fttb.internal.api.spider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OrderDTO {
    private String id;
    private String type;
    @JsonProperty("confirm_time")
    private Date confirmTime;
    private String sn;
    private Integer num;
    private Float price;
    @JsonProperty("image_url")
    private String imageUrl;
}
