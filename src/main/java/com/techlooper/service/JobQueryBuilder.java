package com.techlooper.service;/**
 * Created by phuonghqh on 11/8/14.
 */

import com.techlooper.model.TechnicalTermEnum;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;

public interface JobQueryBuilder {
    static final String[] SEARCH_JOB_FIELDS = new String[]{"jobTitle", "jobDescription", "skillExperience"};
    static final String ES_VIETNAMWORKS_INDEX = "vietnamworks";

    QueryBuilder getTechnicalTermsQuery();

    QueryBuilder getTechnicalTermQuery(TechnicalTermEnum term);

    QueryBuilder getTechnicalSkillQuery(String skill);

    NativeSearchQueryBuilder getVietnamworksJobQuery();

    AggregationBuilder getTechnicalTermAggregation(TechnicalTermEnum term);

    FilterAggregationBuilder getSkillIntervalAggregation(String skill, QueryBuilder skillQuery, String period, Integer interval);

    List<List<FilterAggregationBuilder>> toSkillAggregations(List<String> skills);
}
