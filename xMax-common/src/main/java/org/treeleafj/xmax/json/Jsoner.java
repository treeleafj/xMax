package org.treeleafj.xmax.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * JSON转换工具(默认基于fastjson实现)
 *
 * @author leaf
 * @date 2014-1-3 下午6:09:49
 */
public class Jsoner {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 将一个对象转为json字符窜
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将一个json字符窜转为对象
     */
    public static <T> T toObj(String json, Class<T> classz) {
        try {
            return objectMapper.readValue(json, classz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将一个json字符窜转为对象数组
     */
    public static <T> List<T> toArray(String json, Class<T> classz) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, classz);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}