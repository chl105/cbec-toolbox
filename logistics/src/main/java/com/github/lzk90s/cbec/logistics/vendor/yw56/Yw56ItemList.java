package com.github.lzk90s.cbec.logistics.vendor.yw56;

import lombok.Data;

import java.util.List;

/**
 * @author liuzhikun
 */
@Data
public class Yw56ItemList<T> {
    private int itemCount;
    private List<T> items;
}
