package com.techlooper.service.impl;

import com.techlooper.model.*;
import com.techlooper.service.JobSearchService;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

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
        mergeSearchResultWithConfiguration(jobSearchResponse, getConfiguration());
        return jobSearchResponse;
    }

    //TODO : find a generic and good way to merge search result into configuration later
    private void mergeSearchResultWithConfiguration(
            VNWJobSearchResponse jobSearchResponse, VNWConfigurationResponse configuration) {
        for (VNWJobSearchResponseDataItem responseDataItem : jobSearchResponse.getData().getJobs()) {
            String location = responseDataItem.getLocation();
            String mergeLocation = mergeConfigurationItem(configuration, location, "location");
            responseDataItem.setLocation(mergeLocation);
            String level = responseDataItem.getLevel();
            String mergeLevel = mergeConfigurationItem(configuration, level, "degree");
            responseDataItem.setLevel(mergeLevel);
        }

    }

    private String mergeConfigurationItem(VNWConfigurationResponse configuration, String itemId, String idType) {
        String[] itemIds = StringUtils.split(itemId, ',');
        for(int i = 0; i < itemIds.length; i++) {
            String itemValue = translateConfigurationId(itemIds[i], idType, configuration);
            if (StringUtils.isNotEmpty(itemValue)) {
                itemId = itemId.replaceAll(itemIds[i], itemValue);
            } else {
                itemId = itemId.replaceAll(itemIds[i], StringUtils.EMPTY);
            }
        }
        return itemId;
    }

    private String translateConfigurationId(String id, String itemType, VNWConfigurationResponse configuration) {
        List<? extends VNWConfigurationResponseData.ConfigurationItem> configurationItems;

        switch (itemType) {
            case "location":
                configurationItems = configuration.getData().getLocations();
                for (VNWConfigurationResponseData.ConfigurationItem locationItem : configurationItems) {
                    VNWConfigurationResponseData.ConfigurationLocation location =
                            (VNWConfigurationResponseData.ConfigurationLocation) locationItem;
                    if (location.getLocationId().equals(id)) {
                        return location.getEnglish();
                    }
                }
                break;
            case "level":
                configurationItems = configuration.getData().getDegrees();
                for (VNWConfigurationResponseData.ConfigurationItem degreeItem : configurationItems) {
                    VNWConfigurationResponseData.ConfigurationLocation degree =
                            (VNWConfigurationResponseData.ConfigurationLocation) degreeItem;
                    if (degree.getLocationId().equals(id)) {
                        return degree.getEnglish();
                    }
                }
                break;
        }

        return StringUtils.EMPTY;
    }
}
