package com.github.lzk90s.fttb.logistics.vendor.yw56;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author liuzhikun
 */
@Data
public class Yw56ProductInfo {
    boolean track;
    private boolean calcGweight;
    private boolean cpFlag;
    private int fuelRate;
    private int level1Catalog;
    @JsonProperty("productcode")
    private String produCtcode;
    @JsonProperty("productname")
    private String productName;
    private String quoteType;
    private String saleProductTypeName;
}
