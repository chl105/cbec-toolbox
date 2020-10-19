package com.github.lzk90s.cbec.logistics.vendor.yw56;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.lzk90s.cbec.logistics.model.Price;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author liuzhikun
 */
@Data
public class Yw56Price {
    private String priceCode;
    private String productCode;
    private String rountingCode;
    private List<Yw56Expense> expenseItems;
    private boolean discountFlag;
    private int productFuelRate;
    @JsonProperty("isThrowweight")
    private boolean throwWeight;
    private String priceUdo;
    private Yw56ProductInfo productInfo;

    public float getAllExpense() {
        float sum = 0;
        for (Yw56Expense exp : expenseItems) {
            sum += exp.getMoneyOfOriginal();
        }
        return sum;
    }

    public static class ConverterImpl extends Converter<Yw56Price, Price> {

        @Override
        protected Price doForward(Yw56Price yw56Price) {
            Price obj = new Price();
            BeanUtils.copyProperties(yw56Price, obj);
            obj.setExpense(yw56Price.getAllExpense());
            obj.setProductName(yw56Price.getProductInfo().getProductName());
            return obj;
        }

        @Override
        protected Yw56Price doBackward(Price price) {
            throw new IllegalStateException("无效转换");
        }
    }
}
