package com.techlooper.service;

/**
 * Created by phuonghqh on 11/8/14.
 */

import com.techlooper.model.HistogramEnum;
import com.techlooper.model.Skill;
import com.techlooper.model.TechnicalTerm;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;

public interface JobQueryBuilder {

    static final String[] SEARCH_JOB_FIELDS = new String[]{"jobTitle", "jobDescription", "skillExperience"};

    /**
     * This method will construct that needed to be executed on ES
     *
     * @return {@link org.elasticsearch.index.query.QueryBuilder}
     */
    FilterBuilder getTechnicalTermsQuery();

    /**
     * Constructs the search query based on parameter
     *
     * @param term {@link com.techlooper.model.TechnicalTerm}
     * @return {@link org.elasticsearch.index.query.QueryBuilder}
     */
    FilterBuilder getTechnicalTermQuery(TechnicalTerm term);

    /**
     * Constructs the search query based on parameter
     *
     * @param skill is the detail of term, for example Java is a term and spring is a skill
     * @return {@link org.elasticsearch.index.query.QueryBuilder}
     */
    FilterBuilder getTechnicalSkillQuery(Skill skill);

    /**
     * Creates a query on VietnamWorks ES
     *
     * @return {@link org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder}
     */
    NativeSearchQueryBuilder getVietnamworksJobCountQuery();

    /**
     * Creates a bucket for aggregation of each {@link com.techlooper.model.TechnicalTerm}
     *
     * @param term {@link com.techlooper.model.TechnicalTerm}
     * @return {@link org.elasticsearch.search.aggregations.AggregationBuilder}
     */
    AggregationBuilder getTechnicalTermAggregation(TechnicalTerm term);

    /**
     * @param skills        List of skills which each skill is the detail of term, for example Java is a term and spring is a skill
     * @param histogramEnum {@link com.techlooper.model.HistogramEnum}
     * @return {@link org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder}
     */
    List<List<FilterAggregationBuilder>> toSkillAggregations(List<Skill> skills, HistogramEnum histogramEnum);

    /**
     * @param from joda time value, ex: now-1d , now-1w , now-1M
     * @return {@link org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder}
     */
    FilterBuilder getExpiredDateQuery(String from);

    /**
     * @return {@link org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder}
     */
    FilterBuilder getTechnicalTermsQueryNotExpired();

    /**
     * @param term {@link com.techlooper.model.TechnicalTerm}
     * @return {@link org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder}
     */
    FilterBuilder getTechnicalTermQueryNotExpired(TechnicalTerm term);
}
