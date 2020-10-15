package com.github.lzk90s.fttb.trade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author liuzhikun
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.gitlab.lzk90s.trade")
@EnableFeignClients
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
