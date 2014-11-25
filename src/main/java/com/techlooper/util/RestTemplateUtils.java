package com.techlooper.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
public class RestTemplateUtils {

    /**
     * It builds a {@link org.springframework.http.HttpEntity}
     * @param mediaType See more at {@link org.springframework.http.MediaType}
     * @param apiKey the key
     * @param apiValue the value of API
     * @param requestBody the body
     * @return An instance of {@link org.springframework.http.HttpEntity}
     */
    public static HttpEntity<String> configureHttpRequestEntity(
            MediaType mediaType, String apiKey, String apiValue, String requestBody) {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(mediaType));
        requestHeaders.set(apiKey, apiValue);
        return new HttpEntity<>(requestBody, requestHeaders);
    }
}
