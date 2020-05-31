package com.github.lzk90s.fttb.gateway.model;

import lombok.*;

/**
 * @author :    zj.wu
 * @date :      2019/06/24 10:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RequestContent {
    /**
     * 请求者host
     */
    private String remoteHost;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求path
     */
    private String path;
    /**
     * 请求参数
     */
    private String param;
    /**
     * 响应时间，单位毫秒
     */
    private Long responseTime;
    /**
     * 响应状态
     */
    private int status;
}
