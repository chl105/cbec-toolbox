package com.github.lzk90s.fttb.internal.api.auth;

import lombok.Data;

@Data
public class PlatformAccountDTO {
    private String userName;
    private String platform;
    private String user;
    private String password;
}
