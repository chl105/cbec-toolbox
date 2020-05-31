package com.github.lzk90s.fttb.trade.logistics.vendor.yw56;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author liuzhikun
 */
@Data
public class Yw56Expense {
    @JsonProperty("isReturn")
    private boolean ret;
    private float money;
    private float moneyOfOriginal;
    private int calcType;
    private boolean discount;
    private String expenseItemCode;
    private String expenseItemName;
    private boolean rebate;
    private float cnyMoney;
}
