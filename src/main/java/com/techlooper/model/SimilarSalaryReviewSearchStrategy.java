package com.techlooper.model;

import com.techlooper.entity.JobEntity;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.util.DateTimeUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * Created by NguyenDangKhoa on 12/7/15.
 */
public class SimilarSalaryReviewSearchStrategy extends JobSearchStrategy {

    private SalaryReviewDto salaryReviewDto;

    private ElasticsearchRepository<SalaryReviewEntity, ?> salaryReviewRepository;

    public SimilarSalaryReviewSearchStrategy(SalaryReviewDto salaryReviewDto, ElasticsearchRepository salaryReviewRepository) {
        this.salaryReviewDto = salaryReviewDto;
        this.salaryReviewRepository = salaryReviewRepository;
    }

    @Override
    protected NativeSearchQueryBuilder getSearchQueryBuilder() {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("salaryReview");
        MatchQueryBuilder jobTitleQuery = matchQuery("jobTitle", salaryReviewDto.getJobTitle()).minimumShouldMatch("100%");
        TermsFilterBuilder jobCategoriesFilter = termsFilter("jobCategories", salaryReviewDto.getJobCategories());
        RangeFilterBuilder netSalaryFilter = rangeFilter("netSalary").from(MIN_SALARY_ACCEPTABLE).to(MAX_SALARY_ACCEPTABLE);

        Long sixMonthsAgo = DateTimeUtils.minusPeriod(6, ChronoUnit.MONTHS);
        RangeFilterBuilder sixMonthsAgoFilter = rangeFilter("createdDateTime").from(sixMonthsAgo);

        searchQueryBuilder.withQuery(filteredQuery(jobTitleQuery, boolFilter().must(jobCategoriesFilter)
                .must(netSalaryFilter)
                .must(sixMonthsAgoFilter)));
        return searchQueryBuilder;
    }

    @Override
    protected ElasticsearchRepository<SalaryReviewEntity, ?> getJobRepository() {
        return salaryReviewRepository;
    }

    @Override
    protected List<JobEntity> mapToJobEntities(List<Object> sourceEntities) {
        List<JobEntity> jobEntities = new ArrayList<>();
        for (Object entity : sourceEntities) {
            SalaryReviewEntity salaryReviewEntity = (SalaryReviewEntity) entity;
            JobEntity jobEntity = new JobEntity();
            jobEntity.setId(salaryReviewEntity.getCreatedDateTime().toString());
            jobEntity.setJobTitle(salaryReviewEntity.getJobTitle());
            jobEntity.setSalaryMin(salaryReviewEntity.getNetSalary().longValue());
            jobEntity.setSalaryMax(salaryReviewEntity.getNetSalary().longValue());
            jobEntities.add(jobEntity);
        }
        return jobEntities;
    }
}
