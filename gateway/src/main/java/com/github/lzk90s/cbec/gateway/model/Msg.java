package com.github.lzk90s.cbec.gateway.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Msg<T> implements Serializable {
    private static final long serialVersionUID = -1177183613782210351L;
    private Long timestamp;
    private Integer status;
    private String message;
}
