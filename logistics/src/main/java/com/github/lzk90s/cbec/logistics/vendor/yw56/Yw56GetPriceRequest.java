package com.github.lzk90s.cbec.logistics.vendor.yw56;

import lombok.Builder;
import lombok.Data;

/**
 * @author liuzhikun
 */
@Data
@Builder
public class Yw56GetPriceRequest {
    private String areaCode;
    private String areaName;
    private String cityId;
    private String cityName;
    private String productTypes;
    private String level1Catalog;
    private String countryId;
    private String destination;
    private String weight;
}
