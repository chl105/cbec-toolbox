package com.github.lzk90s.fttb.trade.feign.dto;

import com.github.lzk90s.fttb.trade.model.Category;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CategoryDTO {
    private String name;
    private String url;
    private String description;
    private String vendor;

    public static ConverterImpl getConverter(){
        return new ConverterImpl();
    }

    public static class ConverterImpl extends Converter<CategoryDTO, Category>{

        @Override
        public Category doForward(CategoryDTO categoryDTO) {
            Category category = new Category();
            BeanUtils.copyProperties(categoryDTO, category);
            return category;
        }

        @Override
        public CategoryDTO doBackward(Category category) {
            return null;
        }
    }
}
