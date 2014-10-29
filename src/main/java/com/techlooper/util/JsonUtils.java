package com.techlooper.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
public class JsonUtils {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
    }

    public static ObjectMapper getObjectMapper() {
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return OBJECT_MAPPER;
    }

    public static <T> Optional<T> toPOJO(String json, Class<T> objectType) {
        Optional<T> result = Optional.empty();
        try {
            result = Optional.ofNullable(getObjectMapper().readValue(json, objectType));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static <T> Optional<String> toJSON(T object) {
        Optional<String> result = Optional.empty();
        try {
            result = Optional.ofNullable(getObjectMapper().writeValueAsString(object));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }
}
