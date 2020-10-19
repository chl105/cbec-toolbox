package com.github.lzk90s.cbec.gateway.fallback;

import com.github.lzk90s.cbec.gateway.model.Msg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

/**
 * @author liuzhikun
 * @date 2020/01/08
 */
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {

    private static Msg<Void> ERROR_RESPONSE = new Msg<>();

    @PostConstruct
    private void init() {
        ERROR_RESPONSE.setStatus(503);
        ERROR_RESPONSE.setMessage("服务异常");
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        serverRequest.attribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)
                .ifPresent(originalUrls -> log.error("网关执行请求:{}失败,hystrix服务降级处理", originalUrls));

        return ServerResponse
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(ERROR_RESPONSE));
    }
}