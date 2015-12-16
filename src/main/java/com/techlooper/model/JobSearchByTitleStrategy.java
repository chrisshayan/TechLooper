package com.techlooper.model;

import com.techlooper.entity.JobEntity;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * Created by NguyenDangKhoa on 12/7/15.
 */
public class JobSearchByTitleStrategy extends JobSearchStrategy {

    private String jobTitle;

    private ElasticsearchRepository<JobEntity, ?> repository;

    public JobSearchByTitleStrategy(ElasticsearchRepository repository, String jobTitle) {
        this.jobTitle = jobTitle;
        this.repository = repository;
    }

    @Override
    protected NativeSearchQueryBuilder getSearchQueryBuilder() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withTypes("job");

        QueryBuilder matchQueryBuilder = matchAllQuery();
        if (StringUtils.isNotEmpty(jobTitle)) {
            matchQueryBuilder = jobTitleQueryBuilder(jobTitle);
        }
        queryBuilder.withQuery(filteredQuery(matchQueryBuilder,
                boolFilter().must(getRangeFilterBuilder("approvedDate", "now-6M/M", null))
                        .must(getSalaryRangeFilterBuilder(MIN_SALARY_ACCEPTABLE, MAX_SALARY_ACCEPTABLE))));
        return queryBuilder;
    }

    @Override
    protected ElasticsearchRepository<JobEntity, ?> getJobRepository() {
        return repository;
    }

}
