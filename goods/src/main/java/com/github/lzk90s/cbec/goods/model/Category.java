package com.github.lzk90s.cbec.goods.model;

import com.github.lzk90s.cbec.internal.api.spider.CategoryDTO;
import com.google.common.base.Converter;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class Category {
    private String name;
    private String url;
    private String description;
    private String vendor;

    public static ConverterImpl getConverter() {
        return new ConverterImpl();
    }

    public static class ConverterImpl extends Converter<Category, CategoryDTO> {

        @Override
        public Category doBackward(CategoryDTO categoryDTO) {
            Category category = new Category();
            BeanUtils.copyProperties(categoryDTO, category);
            return category;
        }

        @Override
        public CategoryDTO doForward(Category category) {
            return null;
        }
    }
}
