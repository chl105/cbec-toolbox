package com.github.lzk90s.fttb.gateway.filter;

import com.github.lzk90s.fttb.gateway.model.RequestContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Slf4j
public class PostRequestLogFilter implements GlobalFilter, Ordered {
    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute(START_TIME);
            if (startTime != null) {
                RequestContent requestContent = RequestContent.builder()
                        .method(Objects.requireNonNull(exchange.getRequest().getMethod()).name())
                        .param(exchange.getRequest().getQueryParams().toString())
                        .remoteHost(exchange.getRequest().getURI().getHost())
                        .path(exchange.getRequest().getURI().getPath())
                        .status(Objects.requireNonNull(exchange.getResponse().getStatusCode()).value())
                        .responseTime((System.currentTimeMillis() - startTime))
                        .build();
                log.debug(requestContent.toString());
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

