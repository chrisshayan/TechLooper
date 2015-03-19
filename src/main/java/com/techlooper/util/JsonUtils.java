package com.techlooper.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
public class JsonUtils {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
    }

    /**
     * Returns an instance of {@link com.fasterxml.jackson.databind.ObjectMapper}
     *
     * @return Returns an instance of {@link com.fasterxml.jackson.databind.ObjectMapper}
     */
    public static ObjectMapper getObjectMapper() {
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return OBJECT_MAPPER;
    }

    /**
     * @param json       It is the JSON content
     * @param objectType the class that JSON file will parse to.
     * @param <T>        Generic Type of the class
     * @return An instance of objectType
     */
    public static <T> Optional<T> toPOJO(String json, Class<T> objectType) {
        Optional<T> result = Optional.empty();
        try {
            result = Optional.ofNullable(getObjectMapper().readValue(json, objectType));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Convert a model to JSON
     *
     * @param object the model
     * @param <T>    type of object
     * @return JSON structure
     */
    public static <T> Optional<String> toJSON(T object) {
        Optional<String> result = Optional.empty();
        try {
            result = Optional.ofNullable(getObjectMapper().writeValueAsString(object));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Convert list of JSON to list of objects
     *
     * @param jsonArray  List of JSONs
     * @param objectType the model
     * @param <T>        the type of model
     * @return list of the models
     */
    public static <T> Optional<List<T>> toList(String jsonArray, Class<T> objectType) {
        Optional<List<T>> result = Optional.empty();
        try {
            final CollectionType collectionType =
                    getObjectMapper().getTypeFactory().constructCollectionType(List.class, objectType);
            result = Optional.ofNullable(getObjectMapper().readValue(jsonArray, collectionType));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Convert list of JSON to list of objects
     *
     * @param io         InputStream of JSONs file
     * @param objectType the model
     * @param <T>        the type of model
     * @return list of the models
     */
    public static <T> Optional<List<T>> toList(Resource io, Class<T> objectType) {
        // TODO: use other way to make code shorten
        Optional<List<T>> optional = Optional.empty();
        try {
            String jsonSkill = IOUtils.toString(io.getInputStream(), "UTF-8");
            optional = JsonUtils.toList(jsonSkill, objectType);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return optional;
    }

    public static <K,V> Map<K,V> toMap(Resource io) {
        Map<K,V> map = null;
        try {
            String json = IOUtils.toString(io.getInputStream(), "UTF-8");
            map = getObjectMapper().readValue(json,
                    new TypeReference<HashMap<K,V>>() {});
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return map;
    }
}
