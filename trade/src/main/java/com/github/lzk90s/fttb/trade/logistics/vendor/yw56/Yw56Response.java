package com.github.lzk90s.fttb.trade.logistics.vendor.yw56;

import lombok.Data;

/**
 * @author liuzhikun
 */
@Data
public class Yw56Response<T> {
    private int code;
    private String appId;
    private T result;

    public boolean ok() {
        return code == 0;
    }
}
