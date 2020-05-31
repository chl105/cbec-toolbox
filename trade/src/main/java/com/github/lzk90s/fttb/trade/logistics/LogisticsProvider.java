package com.github.lzk90s.fttb.trade.logistics;

import com.github.lzk90s.fttb.trade.model.Address;
import com.github.lzk90s.fttb.trade.model.Price;

import java.util.List;

/**
 * 物流提供商
 */
public interface LogisticsProvider {
    String getVendor();

    List<Price> getPrice(Address src, Address dst, String types, String goodAttr, float weight);
}
