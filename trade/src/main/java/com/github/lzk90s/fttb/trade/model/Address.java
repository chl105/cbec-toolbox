package com.github.lzk90s.fttb.trade.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author liuzhikun
 */
@Data
@Builder
public class Address {
    /**
     * 国家编码
     */
    private String countryCode;
    /**
     * 国家名
     */
    private String countryName;
    /**
     * 区域编码
     */
    private String areaCode;
    /**
     * 区域名
     */
    private String areaName;
    /**
     * 城市编码
     */
    private String cityCode;
    /**
     * 城市名
     */
    private String cityName;
}
