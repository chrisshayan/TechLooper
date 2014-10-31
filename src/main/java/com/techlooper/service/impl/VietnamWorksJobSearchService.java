package com.techlooper.service.impl;

import com.techlooper.model.VNWConfigurationResponse;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VNWJobSearchResponse;
import com.techlooper.model.VNWJobSearchResponseDataItem;
import com.techlooper.service.JobSearchService;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.techlooper.model.VNWConfigurationResponseData.ConfigurationDegree;
import static com.techlooper.model.VNWConfigurationResponseData.ConfigurationLocation;
import static com.techlooper.model.VNWJobSearchResponseDataItem.JOB_LEVEL;
import static com.techlooper.model.VNWJobSearchResponseDataItem.JOB_LOCATION;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * @author khoa-nd
 * @see JobSearchService
 */
@Service
@CacheConfig(cacheNames = {"vnw"})
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

  /**
   * Get the configuration from Vietnamworks API such as job locations, categories, degree, etc
   *
   * @return The configuration from the API {@link com.techlooper.model.VNWConfigurationResponse}
   */
  @Cacheable
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

  /**
   * Get the job search result from Vietnamworks API which matches the criteria terms
   *
   * @param jobSearchRequest The job search request which contains the criteria terms and page number
   * @return The job search result from the API {@link com.techlooper.model.VNWJobSearchResponse}
   */
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

  /**
   * Merge the search result with configuration in order to get its meaningful name
   *
   * @param jobSearchResponse The job search response
   * @param configuration     The job configuration
   */
  private void mergeSearchResultWithConfiguration(VNWJobSearchResponse jobSearchResponse,
                                                  VNWConfigurationResponse configuration) {
    BiFunction<String, String, String> idTranslator = (itemId, idType) ->
      mergeConfigurationItem(configuration, itemId, idType);

    Stream<VNWJobSearchResponseDataItem> responseDataItemStream = jobSearchResponse.getData().getJobs().stream();
    responseDataItemStream.forEach(responseDataItem -> {
      responseDataItem.setLocation(idTranslator.apply(responseDataItem.getLocation(), JOB_LOCATION));
      responseDataItem.setLevel(idTranslator.apply(responseDataItem.getLevel(), JOB_LEVEL));
    });
  }

  /**
   * Merge the search result with configuration in order to get its meaningful name
   *
   * @param configuration The job configuration
   * @param itemId        List of item IDs should be merged
   * @param idType        The kind of id such as location, level or category
   * @return The item name value after merging, separated by comma
   */
  private String mergeConfigurationItem(VNWConfigurationResponse configuration, String itemId, String idType) {
    final String COMMA = ",";
    Function<String, String> translateConfigurationFunc = (id) -> translateConfigurationId(id, idType, configuration);

    return Stream.of(itemId.split(COMMA)).distinct()
      .map(translateConfigurationFunc).collect(Collectors.joining(COMMA));
  }

  /**
   * Merge the search result with configuration in order to get its meaningful name
   *
   * @param id            Unique ID value
   * @param itemType      The kind of id such as location, level or category
   * @param configuration The job configuration
   * @return The unique item name value after merging
   */
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