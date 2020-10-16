package com.github.lzk90s.fttb.logistics.model;

import lombok.Data;

/**
 * @author liuzhikun
 */
@Data
public class Price {
    private String priceCode;
    private String productCode;
    private float expense;
    private String productName;
}
