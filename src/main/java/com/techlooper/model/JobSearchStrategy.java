package com.techlooper.model;

import com.techlooper.entity.JobEntity;
import com.techlooper.util.DataUtils;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * Created by NguyenDangKhoa on 12/7/15.
 */
public abstract class JobSearchStrategy {

    public static final long MIN_SALARY_ACCEPTABLE = 250L;

    public static final long MAX_SALARY_ACCEPTABLE = 5000L;

    public List<JobEntity> searchJob() {
        List<Object> allEntities = DataUtils.getAllEntities(getJobRepository(), getSearchQueryBuilder());
        return mapToJobEntities(allEntities);
    }

    protected List<JobEntity> mapToJobEntities(List<Object> allEntities) {
        List<JobEntity> jobEntities = new ArrayList<>();
        for (Object entity : allEntities) {
            JobEntity jobEntity = (JobEntity) entity;
            jobEntities.add(jobEntity);
        }
        return jobEntities;
    }

    protected QueryBuilder jobTitleQueryBuilder(String jobTitle) {
        return matchQuery("jobTitle", jobTitle).minimumShouldMatch("100%");
    }

    protected FilterBuilder getJobIndustriesFilterBuilder(List<Long> jobCategories) {
        BoolFilterBuilder jobIndustriesFilterBuilder = boolFilter();
        if (CollectionUtils.isNotEmpty(jobCategories)) {
            jobCategories.forEach(industryId -> jobIndustriesFilterBuilder.should(termFilter("industries.industryId", industryId)));
        }
        return nestedFilter("industries", jobIndustriesFilterBuilder);
    }

    protected FilterBuilder getRangeFilterBuilder(String fieldName, Object fromValue, Object toValue) {
        RangeFilterBuilder rangeFilterBuilder = rangeFilter(fieldName);
        if (fromValue != null) {
            rangeFilterBuilder.from(fromValue);
        }
        if (toValue != null) {
            rangeFilterBuilder.to(toValue);
        }
        return rangeFilterBuilder;
    }

    protected FilterBuilder getSalaryRangeFilterBuilder(Long salaryMin, Long salaryMax) {
        return boolFilter()
                .should(getRangeFilterBuilder("salaryMin", salaryMin, salaryMax))
                .should(getRangeFilterBuilder("salaryMax", salaryMin, salaryMax));
    }

    protected abstract NativeSearchQueryBuilder getSearchQueryBuilder();

    protected abstract <T> ElasticsearchRepository<T, ?> getJobRepository();

}
