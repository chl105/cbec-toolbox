package com.github.lzk90s.fttb.common.exception;

public class BizException extends RuntimeException {
    private int code;

    public BizException(String msg) {
        super(msg);
        this.code = 500;
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
