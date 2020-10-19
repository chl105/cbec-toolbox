package com.github.lzk90s.cbec.internal.api.auth;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfoDTO {
    private Long id;
    private String name;
    private String password;
    private String phone;
    private Boolean state;
    private String email;
    private Date addTime;
}
