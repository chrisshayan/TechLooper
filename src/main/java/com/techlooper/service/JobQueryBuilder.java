package com.techlooper.service;

/**
 * Created by phuonghqh on 11/8/14.
 */

import com.techlooper.entity.PriceJobEntity;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.HistogramEnum;
import com.techlooper.model.Skill;
import com.techlooper.model.TechnicalTerm;
import com.techlooper.model.TermStatisticRequest;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.NestedBuilder;
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
     * @param skill is the detail of term, for example Java is a term and spring is a skill {@link com.techlooper.model.Skill}
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

    FilterBuilder getTechnicalTermQueryAvailableWithinPeriod(String term, HistogramEnum period);

    QueryBuilder getTermQueryBuilder(TermStatisticRequest term);

    FilterBuilder getJobLevelFilterBuilder(List<Integer> jobLevelIds);

    FilterBuilder getJobLevelsFilterBuilder(List<Integer> jobLevelIds);

    FilterAggregationBuilder getTopCompaniesAggregation();

    List<FilterAggregationBuilder> getSkillAnalyticsAggregations(TermStatisticRequest term, HistogramEnum histogramEnum);

    QueryBuilder jobTitleQueryBuilder(String jobTitle);

    FilterBuilder getJobIndustriesFilterBuilder(List<Long> jobCategories);

    FilterBuilder getRangeFilterBuilder(String fieldName, Object fromValue, Object toValue);

    QueryBuilder skillQueryBuilder(List<String> skills);

    FilterBuilder getLocationFilterBuilder(Integer locationId);

    FilterBuilder getSalaryRangeFilterBuilder(Long salaryMin, Long salaryMax);

    NativeSearchQueryBuilder getVietnamworksJobSearchQuery();

    NativeSearchQueryBuilder getJobSearchQueryForSalaryReview(SalaryReviewEntity salaryReviewEntity);

    NativeSearchQueryBuilder getJobSearchQueryBySkill(List<String> skills, List<Long> jobCategories);

    NativeSearchQueryBuilder getSearchQueryForPriceJobReport(PriceJobEntity priceJobEntity);

    NativeSearchQueryBuilder getJobSearchQueryByJobTitle(String jobTitle);

    NativeSearchQueryBuilder getTopDemandedSkillQueryByJobTitle(String jobTitle, List<Long> jobCategories, List<Integer> jobLevelIds);

    NestedBuilder getTopDemandedSkillsAggregation();

    FilterAggregationBuilder getSalaryAverageAggregation(String fieldName);
}
