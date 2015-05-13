package com.techlooper.service.impl;

import com.techlooper.entity.JobEntity;
import com.techlooper.entity.SalaryReview;
import com.techlooper.model.VNWConfigurationResponse;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VNWJobSearchResponse;
import com.techlooper.model.VNWJobSearchResponseDataItem;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobSearchService;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;

/**
 * @author khoa-nd
 * @see JobSearchService
 */
@Service
public class VietnamWorksJobSearchService implements JobSearchService {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private JobSearchAPIConfigurationRepository apiConfiguration;

    private VNWConfigurationResponse configurationResponse;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * Get the configuration from Vietnamworks API such as job locations, categories, degree, etc
     *
     * @return The configuration from the API {@link com.techlooper.model.VNWConfigurationResponse}
     */
    public VNWConfigurationResponse getConfiguration() {
        return Optional.ofNullable(configurationResponse).orElseGet(() -> {
            HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(
                    MediaType.APPLICATION_JSON, apiConfiguration.getApiKeyName(), apiConfiguration.getApiKeyValue(), EMPTY);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    apiConfiguration.getConfigurationUrl(), HttpMethod.GET, requestEntity, String.class);
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
        HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(
                MediaType.APPLICATION_JSON, apiConfiguration.getApiKeyName(), apiConfiguration.getApiKeyValue(), searchParameters);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiConfiguration.getSearchUrl(), HttpMethod.POST, requestEntity, String.class);

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

    public List<JobEntity> getHigherSalaryJobs(SalaryReview salaryReview) {
        NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getVietnamworksJobSearchQuery();

        QueryBuilder jobTitleQueryBuilder = jobQueryBuilder.jobTitleQueryBuilder(salaryReview.getJobTitle());
        FilterBuilder expiredDateFilterBuilder = jobQueryBuilder.getRangeFilterBuilder("expiredDate", "now/d", null);

        queryBuilder.withQuery(filteredQuery(jobTitleQueryBuilder, boolFilter()
                .must(expiredDateFilterBuilder)
                .must(termFilter("isActive", 1))
                .must(termFilter("isApproved", 1))));
        List<JobEntity> higherSalaryJobs = getJobSearchResult(queryBuilder);
        return higherSalaryJobs.stream()
                .filter(job -> getAverageSalary(job.getSalaryMin(), job.getSalaryMax()) > salaryReview.getNetSalary())
                .sorted((job1, job2) -> jobSalaryComparator(job1, job2))
                .limit(3).collect(Collectors.toList());
    }

    private int jobSalaryComparator(JobEntity job1, JobEntity job2) {
        double avgSalaryJob1 = getAverageSalary(job1.getSalaryMin(), job1.getSalaryMax());
        double avgSalaryJob2 = getAverageSalary(job2.getSalaryMin(), job2.getSalaryMax());

        if (avgSalaryJob1 > avgSalaryJob2) {
            return 1;
        } else if (avgSalaryJob2 > avgSalaryJob1) {
            return -1;
        }
        return 0;
    }

    public Double getAverageSalary(Long salaryMin, Long salaryMax) {
        if (salaryMin == 0) {
            return salaryMax * 0.75D;
        } else if (salaryMax == 0) {
            return salaryMin * 1.25D;
        }
        return (salaryMin + salaryMax) / 2D;
    }

    public List<JobEntity> getJobSearchResult(NativeSearchQueryBuilder queryBuilder) {
        queryBuilder.withSearchType(SearchType.DEFAULT);
        long totalJobs = elasticsearchTemplate.count(queryBuilder.build());
        long totalPage = totalJobs % 100 == 0 ? totalJobs / 100 : totalJobs / 100 + 1;
        int pageIndex = 0;
        List<JobEntity> jobs = new ArrayList<>();
        while (pageIndex < totalPage) {
            queryBuilder.withPageable(new PageRequest(pageIndex, 100));
            jobs.addAll(elasticsearchTemplate.queryForPage(queryBuilder.build(), JobEntity.class).getContent());
            pageIndex++;
        }
        return jobs;
    }
}