package com.techlooper.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> T toPOJO(String json, Class<T> objectType) {
        try {
            return getObjectMapper().readValue(json, objectType);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T extends Object> String toJSON(T object) {
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
