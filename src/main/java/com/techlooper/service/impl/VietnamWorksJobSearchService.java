package com.techlooper.service.impl;

import com.techlooper.model.*;
import com.techlooper.service.JobSearchService;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
@Service
public class VietnamWorksJobSearchService implements JobSearchService {

    @Resource
    private RestTemplate restTemplate;

    @Value("${vnw.api.configuration.url}")
    private String configurationUrl;

    @Value("${vnw.api.job.search.url}")
    private String searchUrl;

    @Value("${vnw.api.key.name}")
    private String apiKeyName;

    @Value("${vnw.api.key.value}")
    private String apiKeyValue;

    private VNWConfigurationResponse configurationResponse;

    public VNWConfigurationResponse getConfiguration() {
        if (this.configurationResponse != null) {
            return this.configurationResponse;
        }

        HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(MediaType.APPLICATION_JSON,
                apiKeyName, apiKeyValue, StringUtils.EMPTY);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(configurationUrl, HttpMethod.GET, requestEntity, String.class);
        String configuration = responseEntity.getBody();
        this.configurationResponse = JsonUtils.toPOJO(configuration, VNWConfigurationResponse.class);
        return this.configurationResponse;
    }

    public VNWJobSearchResponse searchJob(VNWJobSearchRequest jobSearchRequest) {
        String searchParameters = JsonUtils.toJSON(jobSearchRequest);
        HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(MediaType.APPLICATION_JSON,
                apiKeyName, apiKeyValue, searchParameters);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(searchUrl, HttpMethod.POST, requestEntity, String.class);

        String jobSearchResult = responseEntity.getBody();
        VNWJobSearchResponse jobSearchResponse = JsonUtils.toPOJO(jobSearchResult, VNWJobSearchResponse.class);

        if (jobSearchResponse != null) {
            mergeSearchResultWithConfiguration(jobSearchResponse, getConfiguration());
        } else {
            jobSearchResponse = VNWJobSearchResponse.getDefaultObject();
        }
        return jobSearchResponse;
    }

    private void mergeSearchResultWithConfiguration(
            VNWJobSearchResponse jobSearchResponse, VNWConfigurationResponse configuration) {
        BiFunction<String, String, String> idTranslator = (itemId, idType) -> {
            return mergeConfigurationItem(configuration, itemId, idType);
        };

        for (VNWJobSearchResponseDataItem responseDataItem : jobSearchResponse.getData().getJobs()) {
            responseDataItem.setLocation(idTranslator.apply(responseDataItem.getLocation(), "location"));
            responseDataItem.setLevel(idTranslator.apply(responseDataItem.getLevel(), "degree"));
        }

    }

    private String mergeConfigurationItem(VNWConfigurationResponse configuration, String itemId, String idType) {
        final char COMMA = ',';
        String[] itemIds = StringUtils.split(itemId, COMMA);
        Function<String, String> translateConfigurationFunc = (id) -> {
            return translateConfigurationId(id, idType, configuration);
        };

        String[] translatedIds = Arrays.stream(itemIds).distinct().map(
                translateConfigurationFunc).toArray(size -> new String[size]);
        return StringUtils.join(translatedIds, COMMA);
    }

    private String translateConfigurationId(String id, String itemType, VNWConfigurationResponse configuration) {
        switch (itemType) {
            case "location":
                VNWConfigurationResponseData.ConfigurationLocation locationValue =
                        configuration.getData().getLocations().stream()
                                .filter(item -> item.getLocationId().equals(id))
                                .findFirst().orElse(null);
                return locationValue != null ? locationValue.getEnglish() : StringUtils.EMPTY;
            case "degree":
                VNWConfigurationResponseData.ConfigurationDegree degreeValue =
                        configuration.getData().getDegrees().stream()
                                .filter(item -> item.getDegreeId().equals(id))
                                .findFirst().orElse(null);
                return degreeValue != null ? degreeValue.getEnglish() : StringUtils.EMPTY;
        }
        return StringUtils.EMPTY;
    }
}
