package com.techlooper.service.impl;

import com.techlooper.model.VNWConfigurationResponse;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VNWJobSearchResponse;
import com.techlooper.model.VNWJobSearchResponseDataItem;
import com.techlooper.service.JobSearchService;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.techlooper.model.VNWConfigurationResponseData.ConfigurationDegree;
import static com.techlooper.model.VNWConfigurationResponseData.ConfigurationLocation;
import static com.techlooper.model.VNWJobSearchResponseDataItem.JOB_LEVEL;
import static com.techlooper.model.VNWJobSearchResponseDataItem.JOB_LOCATION;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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
        return Optional.ofNullable(configurationResponse).orElseGet(() -> {
            HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(APPLICATION_JSON, apiKeyName, apiKeyValue, EMPTY);
            ResponseEntity<String> responseEntity = restTemplate.exchange(configurationUrl, HttpMethod.GET, requestEntity, String.class);
            final Optional<String> configuration = Optional.ofNullable(responseEntity.getBody());

            if (configuration.isPresent()) {
                configurationResponse = JsonUtils.toPOJO(configuration.get(), VNWConfigurationResponse.class).
                        orElseGet(VNWConfigurationResponse::new);
            }
            return configurationResponse;
        });
    }

    public VNWJobSearchResponse searchJob(VNWJobSearchRequest jobSearchRequest) {
        final String searchParameters = JsonUtils.toJSON(jobSearchRequest).orElse(EMPTY);
        HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(APPLICATION_JSON, apiKeyName, apiKeyValue, searchParameters);
        ResponseEntity<String> responseEntity = restTemplate.exchange(searchUrl, HttpMethod.POST, requestEntity, String.class);

        final Optional<String> jobSearchResponseJson = Optional.ofNullable(responseEntity.getBody());

        if (jobSearchResponseJson.isPresent()) {
            final VNWJobSearchResponse actualResult = JsonUtils.toPOJO(jobSearchResponseJson.get(), VNWJobSearchResponse.class)
                    .orElse(VNWJobSearchResponse.getDefaultObject());

            if (actualResult.hasData()) {
                mergeSearchResultWithConfiguration(actualResult, getConfiguration());
                return actualResult;
            }
        }
        return VNWJobSearchResponse.getDefaultObject();
    }

    private void mergeSearchResultWithConfiguration(VNWJobSearchResponse jobSearchResponse,
                                                    VNWConfigurationResponse configuration) {
        BiFunction<String, String, String> idTranslator = (itemId, idType) ->
                mergeConfigurationItem(configuration, itemId, idType);

        for (VNWJobSearchResponseDataItem responseDataItem : jobSearchResponse.getData().getJobs()) {
            responseDataItem.setLocation(idTranslator.apply(responseDataItem.getLocation(), JOB_LOCATION));
            responseDataItem.setLevel(idTranslator.apply(responseDataItem.getLevel(), JOB_LEVEL));
        }

    }

    private String mergeConfigurationItem(VNWConfigurationResponse configuration, String itemId, String idType) {
        final char COMMA = ',';
        String[] itemIds = StringUtils.split(itemId, COMMA);
        Function<String, String> translateConfigurationFunc = (id) -> translateConfigurationId(id, idType, configuration);

        String[] translatedIds = Arrays.stream(itemIds).distinct().map(
                translateConfigurationFunc).toArray(String[]::new);
        return StringUtils.join(translatedIds, COMMA);
    }

    private String translateConfigurationId(String id, String itemType, VNWConfigurationResponse configuration) {
        switch (itemType) {
            case JOB_LOCATION:
                Optional<ConfigurationLocation> locationOptional = configuration.getData().getLocations().stream()
                        .filter(item -> item.getLocationId().equals(id))
                        .findFirst();
                return locationOptional.isPresent() ? locationOptional.get().getEnglish() : EMPTY;
            case JOB_LEVEL:
                Optional<ConfigurationDegree> degreeOptional =
                        configuration.getData().getDegrees().stream()
                                .filter(item -> item.getDegreeId().equals(id))
                                .findFirst();
                return degreeOptional.isPresent() ? degreeOptional.get().getEnglish() : EMPTY;
        }
        return EMPTY;
    }
}
