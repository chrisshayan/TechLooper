package com.techlooper.service.impl;

import com.techlooper.model.HistogramEnum;
import com.techlooper.model.Skill;
import com.techlooper.model.TechnicalTerm;
import com.techlooper.model.TermStatisticRequest;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.util.EncryptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesAggregator;
import org.elasticsearch.search.aggregations.metrics.percentiles.PercentilesBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by phuonghqh on 11/8/14.
 */
@Component
public class JobQueryBuilderImpl implements JobQueryBuilder {

    @Value("${elasticsearch.index.name}")
    private String elasticSearchIndexName;

    @Value("${vnw.api.configuration.category.it.software.en}")
    private String itSoftwareIndustry;

    @Resource
    private JsonConfigRepository jsonConfigRepository;

    public FilterBuilder getTechnicalTermsQuery() {
        BoolFilterBuilder boolFilter = boolFilter();
        jsonConfigRepository.getSkillConfig().stream().map(this::getTechnicalTermQuery).forEach(boolFilter::should);
        return boolFilter;
    }

    public FilterBuilder getTechnicalTermQuery(TechnicalTerm term) {
        BoolFilterBuilder boolFilter = boolFilter();
        term.getSearchTexts().stream().map(termName -> getTechnicalTermQuery(termName)).forEach(boolFilter::should);
        return boolFilter;
    }

    public FilterBuilder getTechnicalTermQuery(String term) {
        return queryFilter(multiMatchQuery(term, SEARCH_JOB_FIELDS)
                .operator(Operator.AND));
    }

    public FilterBuilder getTechnicalSkillQuery(Skill skill) {
        return queryFilter(
                multiMatchQuery(skill.getName(), SEARCH_JOB_FIELDS).operator(Operator.AND)).cache(true);
    }

    public NativeSearchQueryBuilder getVietnamworksJobCountQuery() {
        return new NativeSearchQueryBuilder().withIndices(elasticSearchIndexName).withTypes("job").withSearchType(SearchType.COUNT);
    }

    public AggregationBuilder getTechnicalTermAggregation(TechnicalTerm term) {
        return AggregationBuilders.filter(term.getKey()).filter(this.getTechnicalTermQuery(term));
    }

    /**
     * Constructs a query based on a specific period.
     *
     * @param skill         is the detail of term, for example Java is a term and spring is a skill
     * @param skillQuery    {@link org.elasticsearch.index.query.QueryBuilder}
     * @param histogramEnum {@link com.techlooper.model.HistogramEnum}
     * @param total         {@link com.techlooper.model.HistogramEnum#getTotal()}
     * @return {@link org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder}
     */
    private FilterAggregationBuilder getSkillIntervalAggregation(Skill skill, FilterBuilder skillQuery,
                                                                 HistogramEnum histogramEnum, Integer total) {
        String intervalDate = LocalDate.now().minusDays(total).format(DateTimeFormatter.ofPattern("YYYYMMdd"));
        String to = "now-" + (total * histogramEnum.getPeriod()) + histogramEnum.getUnit();
        RangeFilterBuilder approveDateQuery = rangeFilter("approvedDate").to(to).cache(true);
        RangeFilterBuilder expiredDateQuery = rangeFilter("expiredDate").gte(to).cache(true);
        BoolFilterBuilder filterQuery = boolFilter().must(skillQuery, approveDateQuery, expiredDateQuery);
        return AggregationBuilders.filter(EncryptionUtils.encodeHexa(skill.getName()) + "-" + histogramEnum + "-" + intervalDate).filter(filterQuery);
    }

    public List<List<FilterAggregationBuilder>> toSkillAggregations(List<Skill> skills, HistogramEnum histogramEnum) {
        Integer total = histogramEnum.getTotal();
        return skills.stream().map(skill -> {
            FilterBuilder skillQuery = this.getTechnicalSkillQuery(skill);
            List<FilterAggregationBuilder> builders = new LinkedList<>();
            for (int histogramEnumLengthCounter = 0; histogramEnumLengthCounter < total; ++histogramEnumLengthCounter) {
                builders.add(this.getSkillIntervalAggregation(skill, skillQuery, histogramEnum, histogramEnumLengthCounter));
            }
            return builders;
        }).collect(toList());
    }

    public FilterBuilder getExpiredDateQuery(String from) {
        return rangeFilter("expiredDate").from(from).cache(true);
    }

    public FilterBuilder getTechnicalTermsQueryNotExpired() {
        BoolFilterBuilder allTerms = (BoolFilterBuilder) this.getTechnicalTermsQuery();
        return allTerms.must(this.getExpiredDateQuery("now"));
    }

    public FilterBuilder getTechnicalTermQueryNotExpired(TechnicalTerm term) {
        return boolFilter().must(this.getTechnicalTermQuery(term), this.getExpiredDateQuery("now"));
    }

    public FilterBuilder getTechnicalTermQueryAvailableWithinPeriod(String term, HistogramEnum period) {
        String lastPeriod = "now-" + period.getPeriod() + period.getUnit();
        FilterBuilder periodFilter = rangeFilter("approvedDate").from(lastPeriod).to("now").cache(true);
        return boolFilter().must(this.getTechnicalTermQuery(term), periodFilter);
    }

    @Override
    public QueryBuilder getTermQueryBuilder(TermStatisticRequest term) {
        List<String> searchTexts = jsonConfigRepository.findByKey(term.getTerm()).getSearchTexts();
        String querySearchText = StringUtils.join(searchTexts.toArray(), ' ');
        MultiMatchQueryBuilder termQueryBuilder = multiMatchQuery(querySearchText, SEARCH_JOB_FIELDS).operator(Operator.OR);
        return termQueryBuilder;
    }

    @Override
    public FilterBuilder getJobLevelFilterBuilder(List<Integer> jobLevelIds) {
        BoolFilterBuilder jobLevelFilterBuilder = FilterBuilders.boolFilter();
        jobLevelFilterBuilder.must(nestedFilter("industries", termFilter("industries.industryId", itSoftwareIndustry)));
        for (Integer jobLevelId : jobLevelIds) {
            jobLevelFilterBuilder.should(termFilter("jobLevelId", jobLevelId));
        }
        return jobLevelFilterBuilder;
    }

    @Override
    public FilterBuilder getJobLevelsFilterBuilder(List<Integer> jobLevelIds) {
        BoolFilterBuilder jobLevelFilterBuilder = boolFilter().should(termFilter("jobLevelId", itSoftwareIndustry));
        for (Integer jobLevelId : jobLevelIds) {
            jobLevelFilterBuilder.should(termFilter("jobLevelId", jobLevelId));
        }
        return jobLevelFilterBuilder;
    }

    @Override
    public FilterAggregationBuilder getTopCompaniesAggregation() {
        return AggregationBuilders.filter("top_companies").filter(rangeFilter("expiredDate").from("now")).subAggregation(
                AggregationBuilders.terms("top_companies").field("companyId"));
    }

    @Override
    public List<FilterAggregationBuilder> getSkillAnalyticsAggregations(TermStatisticRequest term, HistogramEnum histogramEnum) {
        String lastPeriod = histogramEnum.getTotal() * histogramEnum.getPeriod() + histogramEnum.getUnit();
        List<FilterAggregationBuilder> skillAnalyticsAggregations = new ArrayList<>();
        for (String skill : term.getSkills()) {
            String aggName = EncryptionUtils.encodeHexa(skill) + "_" + histogramEnum + "_analytics";
            BoolQueryBuilder skillQueryBuilder = boolQuery().should(matchPhraseQuery("jobTitle", skill))
                    .should(matchPhraseQuery("jobDescription", skill))
                    .should(matchPhraseQuery("skillExperience", skill));
            FilterBuilder skillFilter = queryFilter(boolQuery()
                    .must(skillQueryBuilder)
                    .must(rangeQuery("approvedDate").from("now-" + lastPeriod)));
            AggregationBuilder skillHistogramAgg = AggregationBuilders.dateHistogram(aggName)
                    .field("approvedDate").format("yyyy-MM-dd").interval(DateHistogram.Interval.MONTH).minDocCount(0);

            skillAnalyticsAggregations.add(AggregationBuilders.filter(aggName).filter(skillFilter).subAggregation(skillHistogramAgg));
        }
        return skillAnalyticsAggregations;
    }

    @Override
    public QueryBuilder jobTitleQueryBuilder(String jobTitle) {
        return matchQuery("jobTitle", jobTitle).minimumShouldMatch("100%");
    }

    @Override
    public FilterBuilder getJobIndustriesFilterBuilder(List<Long> jobCategories) {
        BoolFilterBuilder jobIndustriesFilterBuilder = boolFilter();
        if (!jobCategories.isEmpty()) {
            jobCategories.forEach(industryId -> jobIndustriesFilterBuilder.should(termFilter("industries.industryId", industryId)));
        }
        return nestedFilter("industries", jobIndustriesFilterBuilder);
    }

    @Override
    public FilterBuilder getRangeFilterBuilder(String fieldName, Object fromValue, Object toValue) {
        RangeFilterBuilder rangeFilterBuilder = rangeFilter(fieldName);
        if (fromValue != null) {
            rangeFilterBuilder.from(fromValue);
        }
        if (toValue != null) {
            rangeFilterBuilder.to(toValue);
        }
        return rangeFilterBuilder;
    }

    @Override
    public PercentilesBuilder salaryPercentileAggregationBuilder(double[] percents) {
        String script = "doc['salaryMin'].value == 0 ? doc['salaryMax'].value * 0.75 : " +
                "doc['salaryMax'].value == 0 ? doc['salaryMin'].value * 1.25 : " +
                "(doc['salaryMin'].value + doc['salaryMax'].value) / 2";
        return AggregationBuilders.percentiles("salary_percentile")
                .script(script).percentiles(percents).compression(100D);
    }
}
