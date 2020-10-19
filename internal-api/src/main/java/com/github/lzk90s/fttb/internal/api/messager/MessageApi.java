package com.github.lzk90s.fttb.internal.api.messager;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/internal/message")
public interface MessageApi {
    void sendMail(String dstMail, String message);

    void sendWechat(String wechatNumber, String message);
}
