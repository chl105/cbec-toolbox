package com.github.lzk90s.fttb.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;


public class JsonUtil {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    static {
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(FORMAT_PATTERN));
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }

    /**
     * javaBean,list,array convert to json string
     */
    public static String obj2json(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T json2pojo(String jsonStr, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T json2pojo(String jsonStr, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * json string convert to map
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> json2map(String jsonStr) {
        try {
            return OBJECT_MAPPER.readValue(jsonStr, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * map convert to javaBean
     */
    public static <T> T map2pojo(Map<?, ?> map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }
}
