package com.github.lzk90s.fttb.internal.api.messager;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/internal/message")
public interface MessageApi {
    @PostMapping("/mail")
    void sendMail(@RequestBody MessageDTO message);

    @PostMapping("/wechat")
    void sendWechat(@RequestBody MessageDTO message);
}
