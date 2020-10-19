package com.github.lzk90s.cbec.common.exception;

import com.github.lzk90s.cbec.common.rest.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestAdviceHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestAdviceHandler.class);

    private static final String DIVIDER = ",";

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result baseBusinessException(BizException e) {
        logger.error(e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result illegalArgumentException(IllegalArgumentException e) {
        logger.error(e.getMessage(), e);
        return Result.error(400, "非法参数: " + e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result nullPointerException(NullPointerException e) {
        logger.error(e.getMessage(), e);
        return Result.error(500, "服务内部错误(NullPointer)");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result runtimeException(RuntimeException e) {
        logger.error(e.getMessage(), e);
        return Result.error(500, e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result defaultHandler(Throwable e) {
        logger.error(e.getMessage(), e);
        return Result.error(500, "服务内部错误");
    }
}
