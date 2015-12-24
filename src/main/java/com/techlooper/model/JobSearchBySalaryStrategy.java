package com.techlooper.model;

import com.techlooper.entity.JobEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * Created by NguyenDangKhoa on 12/7/15.
 */
public class JobSearchBySalaryStrategy extends JobSearchStrategy {

    private SalaryReviewCondition salaryReviewCondition;

    private ElasticsearchRepository<JobEntity, ?> repository;

    public JobSearchBySalaryStrategy(SalaryReviewCondition salaryReviewCondition, ElasticsearchRepository elasticsearchRepository) {
        this.salaryReviewCondition = salaryReviewCondition;
        this.repository = elasticsearchRepository;
    }

    @Override
    protected NativeSearchQueryBuilder getSearchQueryBuilder() {
        String jobTitle = salaryReviewCondition.getJobTitle();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withTypes("job");
        BoolFilterBuilder boolFilterBuilder = boolFilter();

        if (CollectionUtils.isNotEmpty(salaryReviewCondition.getJobCategories())) {
            FilterBuilder jobIndustriesFilterBuilder = getJobIndustriesFilterBuilder(salaryReviewCondition.getJobCategories());
            boolFilterBuilder.must(jobIndustriesFilterBuilder);
        }
        FilterBuilder approvedDateRangeFilterBuilder = getRangeFilterBuilder("approvedDate", "now-6M/M", "now");
        boolFilterBuilder.must(approvedDateRangeFilterBuilder);
        FilterBuilder salaryRangeFilterBuilder = getSalaryRangeFilterBuilder(MIN_SALARY_ACCEPTABLE, MAX_SALARY_ACCEPTABLE);
        boolFilterBuilder.must(salaryRangeFilterBuilder);

        QueryBuilder matchQueryBuilder = matchAllQuery();
        if (StringUtils.isNotEmpty(jobTitle)) {
            matchQueryBuilder = jobTitleQueryBuilder(jobTitle);
        }
        queryBuilder.withQuery(filteredQuery(matchQueryBuilder, boolFilterBuilder));
        return queryBuilder;
    }

    @Override
    protected ElasticsearchRepository<JobEntity, ?> getJobRepository() {
        return repository;
    }

}
