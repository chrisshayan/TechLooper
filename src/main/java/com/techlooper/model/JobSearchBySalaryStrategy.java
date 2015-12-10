package com.techlooper.model;

import com.techlooper.entity.JobEntity;
import com.techlooper.util.DataUtils;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by NguyenDangKhoa on 12/7/15.
 */
public class JobSearchBySalaryStrategy extends JobSearchStrategy {

    private SalaryReviewDto salaryReviewDto;

    private ElasticsearchRepository<JobEntity, ?> repository;

    public JobSearchBySalaryStrategy(SalaryReviewDto salaryReviewDto, ElasticsearchRepository elasticsearchRepository) {
        this.salaryReviewDto = salaryReviewDto;
        this.repository = elasticsearchRepository;
    }

    @Override
    protected NativeSearchQueryBuilder getSearchQueryBuilder() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withTypes("job");

        //pre-process job title in case user enters multiple roles of his job
        List<String> jobTitleTokens = DataUtils.preprocessJobTitle(salaryReviewDto.getJobTitle());

        BoolQueryBuilder boolQueryBuilder = boolQuery();
        BoolFilterBuilder boolFilterBuilder = boolFilter();
        jobTitleTokens.forEach(jobTitleToken -> boolQueryBuilder.should(jobTitleQueryBuilder(jobTitleToken.trim())));

        if (CollectionUtils.isNotEmpty(salaryReviewDto.getJobCategories())) {
            FilterBuilder jobIndustriesFilterBuilder = getJobIndustriesFilterBuilder(salaryReviewDto.getJobCategories());
            boolFilterBuilder.must(jobIndustriesFilterBuilder);
        }
        FilterBuilder approvedDateRangeFilterBuilder = getRangeFilterBuilder("approvedDate", "now-6M/M", "now");
        boolFilterBuilder.must(approvedDateRangeFilterBuilder);
        FilterBuilder salaryRangeFilterBuilder = getSalaryRangeFilterBuilder(MIN_SALARY_ACCEPTABLE, MAX_SALARY_ACCEPTABLE);
        boolFilterBuilder.must(salaryRangeFilterBuilder);

        QueryBuilder matchQueryBuilder = matchAllQuery();
        if (CollectionUtils.isNotEmpty(jobTitleTokens)) {
            matchQueryBuilder = boolQueryBuilder;
        }
        queryBuilder.withQuery(filteredQuery(matchQueryBuilder, boolFilterBuilder));
        return queryBuilder;
    }

    @Override
    protected ElasticsearchRepository<JobEntity, ?> getJobRepository() {
        return repository;
    }

}
