package com.github.lzk90s.fttb.trade.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
    public static String getUserName() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        }
        return username;
    }
}
