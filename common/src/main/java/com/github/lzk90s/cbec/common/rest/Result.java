package com.github.lzk90s.cbec.common.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Restful接口统一响应消息格式
 *
 * @param <T>
 * @author liuzk
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Integer OK = 0;
    private static final String DEFAULT_MESSAGE = "";

    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 状态码
     *
     * @mock 0
     */
    private Integer status = OK;
    /**
     * 消息
     *
     * @mock
     */
    private String message = DEFAULT_MESSAGE;
    /**
     * 数据
     */
    private T data = null;
    /**
     * 数组总条数（仅分页查询使用）
     */
    private Long total = null;
    /**
     * 当前页数据条数（仅分页查询使用）
     */
    private Long row = null;
    /**
     * 扩展字段
     */
    private Map<String, Object> extend = null;


    /**
     * 设置为private，禁止外部直接new
     */
    private Result() {
    }

    /*-------------------------------------------------*/
    public static <T> Result<T> ok() {
        return new Result<T>().
                status(OK).
                body(null).
                putTimeStamp();
    }

    public static <T> Result<T> ok(T data) {
        return new Result<T>().
                status(OK).
                body(data).
                putTimeStamp();
    }

    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
    /*-------------------------------------------------*/

    public static <T> Result<T> error(int status, String message) {
        return new Result<T>().
                status(status).
                message(message).
                putTimeStamp();
    }

    @JsonIgnore
    public boolean isOk() {
        return this.status.equals(OK);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Long getRow() {
        return row;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        if (data instanceof List) {
            int size = ((List) data).size();
            setListSize(size);
            Long total = this.total;
            if (total == null || total < size) {
                setTotal(size);
            }
        }
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Result<T> status(int status) {
        this.status = status;
        return this;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> body(T data) {
        setData(data);
        return this;
    }

    public Result<T> total(long total) {
        setTotal(total);
        return this;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

    public Object getExtend(String key) {
        if (extend == null) {
            return null;
        }

        return extend.get(key);
    }

    public Result<T> addExtend(Map<String, Object> extend) {
        if (extend == null) {
            return this;
        }

        if (this.extend == null) {
            this.extend = new HashMap<>(extend.size());
        }

        this.extend.putAll(extend);
        return this;
    }

    public Result<T> addExtend(String key, Object object) {
        if (extend == null) {
            extend = new HashMap<>(16);
        }
        extend.put(key, object);
        return this;
    }

    private Result<T> putTimeStamp() {
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    private void setListSize(long size) {
        this.row = size;
    }
}
