package com.github.lzk90s.cbec.common.config;

import com.github.lzk90s.cbec.common.exception.RestAdviceHandler;
import org.springframework.context.annotation.Bean;

public class BeanConfiguration {
    @Bean
    public RestAdviceHandler restExceptionAdvice() {
        return new RestAdviceHandler();
    }
}
