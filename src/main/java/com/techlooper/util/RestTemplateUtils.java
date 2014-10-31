package com.techlooper.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
public class RestTemplateUtils {

    public static HttpEntity<String> configureHttpRequestEntity(
            MediaType mediaType, String apiKey, String apiValue, String requestBody) {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(mediaType));
        requestHeaders.set(apiKey, apiValue);
        return new HttpEntity<>(requestBody, requestHeaders);
    }
}
