package com.github.lzk90s.fttb.messager.controller.internal;

import com.github.lzk90s.fttb.internal.api.messager.MessageApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@RestController
@RequestMapping("/internal/message")
public class MessageApiImpl implements MessageApi {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendMail(String dstMail, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(dstMail);
            helper.setSubject("跨境电商: 通知消息");
            helper.setText(message, true);
            mailSender.send(mimeMessage);
            log.info("发送邮件成功");
        } catch (MessagingException e) {
            log.error("发送邮件失败", e);
        }
    }

    @Override
    public void sendWechat(String wechatNumber, String message) {
        throw new UnsupportedOperationException();
    }
}
