package com.techlooper.service.impl;

import com.techlooper.entity.JobEntity;
import com.techlooper.model.SalaryReviewDto;
import com.techlooper.model.VNWConfigurationResponse;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VNWJobSearchResponse;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.repository.elasticsearch.VietnamworksJobRepository;
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
import java.util.stream.Collectors;

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

    @Resource
    private VietnamworksJobRepository vietnamworksJobRepository;

    private static final long VIETNAM_CURRENCY_SALARY_DETECTOR = 1000000L;

    private static final long VND_USD_RATE = 21000L;

    private static final int SORT_ORDER_DESC = -1;

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
                return actualResult;
            }
        }
        return VNWJobSearchResponse.getDefaultObject();
    }

    public List<JobEntity> getHigherSalaryJobs(SalaryReviewDto salaryReviewDto) {
        NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getVietnamworksJobSearchQuery();

        QueryBuilder jobTitleQueryBuilder = jobQueryBuilder.jobTitleQueryBuilder(salaryReviewDto.getJobTitle());
        FilterBuilder expiredDateFilterBuilder = jobQueryBuilder.getRangeFilterBuilder("expiredDate", "now/d", null);

        queryBuilder.withQuery(filteredQuery(jobTitleQueryBuilder, boolFilter()
                .must(expiredDateFilterBuilder)
                .must(termFilter("isActive", 1))
                .must(termFilter("isApproved", 1))
                .must(jobQueryBuilder.getJobIndustriesFilterBuilder(salaryReviewDto.getJobCategories()))));
        List<JobEntity> higherSalaryJobs = getJobSearchResult(queryBuilder);
        return higherSalaryJobs.stream()
                .filter(job -> getAverageSalary(job.getSalaryMin(), job.getSalaryMax()) > salaryReviewDto.getNetSalary())
                .sorted((job1, job2) -> jobSalaryComparator(job1, job2, SORT_ORDER_DESC))
                .limit(3).collect(Collectors.toList());
    }

    private int jobSalaryComparator(JobEntity job1, JobEntity job2, int sortOrder) {
        double avgSalaryJob1 = getAverageSalary(job1.getSalaryMin(), job1.getSalaryMax());
        double avgSalaryJob2 = getAverageSalary(job2.getSalaryMin(), job2.getSalaryMax());

        if (avgSalaryJob1 > avgSalaryJob2) {
            return 1 * sortOrder;
        } else if (avgSalaryJob2 > avgSalaryJob1) {
            return -1 * sortOrder;
        }
        return 0;
    }

    public Double getAverageSalary(Long salaryMin, Long salaryMax) {
        Long salaryMinUsd = convertVND2ToUSD(salaryMin);
        Long salaryMaxUsd = convertVND2ToUSD(salaryMax);
        if (salaryMinUsd == 0) {
            return salaryMaxUsd * 0.75D;
        } else if (salaryMaxUsd == 0) {
            return salaryMinUsd * 1.25D;
        }
        return (salaryMinUsd + salaryMaxUsd) / 2D;
    }

    @Override
    public JobEntity findJobById(String id) {
        return vietnamworksJobRepository.findOne(id);
    }

    @Override
    public JobEntity updateJob(JobEntity jobEntity) {
        return vietnamworksJobRepository.save(jobEntity);
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

    private Long convertVND2ToUSD(Long salary) {
        if (salary == null) {
            return 0L;
        }
        return salary > VIETNAM_CURRENCY_SALARY_DETECTOR ? salary / VND_USD_RATE : salary;
    }
}