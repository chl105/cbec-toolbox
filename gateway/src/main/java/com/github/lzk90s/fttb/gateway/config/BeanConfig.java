package com.github.lzk90s.fttb.gateway.config;

import org.slf4j.helpers.BasicMarkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public BasicMarkerFactory basicMarkerFactory() {
        return new BasicMarkerFactory();
    }
}
