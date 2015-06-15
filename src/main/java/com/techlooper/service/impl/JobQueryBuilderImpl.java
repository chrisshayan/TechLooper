package com.techlooper.service.impl;

import com.techlooper.entity.PriceJobEntity;
import com.techlooper.entity.SalaryReviewEntity;
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
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregator;
import org.elasticsearch.search.aggregations.bucket.nested.NestedBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.techlooper.service.impl.SalaryReviewServiceImpl.*;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.aggregations.AggregationBuilders.*;

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
        return filter(term.getKey()).filter(this.getTechnicalTermQuery(term));
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
        return filter(EncryptionUtils.encodeHexa(skill.getName()) + "-" + histogramEnum + "-" + intervalDate).filter(filterQuery);
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
        BoolFilterBuilder jobLevelFilterBuilder = boolFilter();
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
        return filter("top_companies").filter(rangeFilter("expiredDate").from("now")).subAggregation(
                terms("top_companies").field("companyId"));
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
            AggregationBuilder skillHistogramAgg = dateHistogram(aggName)
                    .field("approvedDate").format("yyyy-MM-dd").interval(DateHistogram.Interval.MONTH).minDocCount(0);

            skillAnalyticsAggregations.add(filter(aggName).filter(skillFilter).subAggregation(skillHistogramAgg));
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
    public QueryBuilder skillQueryBuilder(List<String> skills) {
        List<String> analyzedSkills = processSkillsBeforeSearch(skills);
        BoolQueryBuilder skillQueryBuilder = boolQuery();
        for (String skill : analyzedSkills) {
            skillQueryBuilder.should(matchQuery("skills.skillName", skill).minimumShouldMatch("100%"));
        }
        return QueryBuilders.nestedQuery("skills", skillQueryBuilder);
    }

    @Override
    public FilterBuilder getLocationFilterBuilder(Integer locationId) {
        return termFilter("cityList", locationId);
    }

    @Override
    public FilterBuilder getSalaryRangeFilterBuilder(Long salaryMin, Long salaryMax) {
        return boolFilter()
                .should(getRangeFilterBuilder("salaryMin", salaryMin, salaryMax))
                .should(getRangeFilterBuilder("salaryMax", salaryMin, salaryMax));
    }

    @Override
    public NativeSearchQueryBuilder getVietnamworksJobSearchQuery() {
        return new NativeSearchQueryBuilder().withIndices(elasticSearchIndexName).withTypes("job");
    }

    @Override
    public NativeSearchQueryBuilder getJobSearchQueryForSalaryReview(SalaryReviewEntity salaryReviewEntity) {
        NativeSearchQueryBuilder queryBuilder = getVietnamworksJobCountQuery();

        //pre-process job title in case user enters multiple roles of his job
        List<String> jobTitleTokens = preprocessJobTitle(salaryReviewEntity.getJobTitle());

        BoolQueryBuilder jobTitleQueryBuilder = boolQuery();
        jobTitleTokens.forEach(jobTitleToken -> jobTitleQueryBuilder.should(jobTitleQueryBuilder(jobTitleToken.trim())));
        FilterBuilder jobIndustriesFilterBuilder = getJobIndustriesFilterBuilder(salaryReviewEntity.getJobCategories());
        FilterBuilder approvedDateRangeFilterBuilder = getRangeFilterBuilder("approvedDate", "now-6M/M", null);
        FilterBuilder salaryRangeFilterBuilder = getSalaryRangeFilterBuilder(MIN_SALARY_ACCEPTABLE, MAX_SALARY_ACCEPTABLE);

        queryBuilder.withQuery(filteredQuery(jobTitleQueryBuilder,
                boolFilter().must(approvedDateRangeFilterBuilder)
                            .must(jobIndustriesFilterBuilder)
                            .must(salaryRangeFilterBuilder)));
        return queryBuilder;
    }

    @Override
    public NativeSearchQueryBuilder getJobSearchQueryBySkill(List<String> skills, List<Long> jobCategories) {
        NativeSearchQueryBuilder queryBuilder = getVietnamworksJobCountQuery();

        QueryBuilder skillQueryBuilder = skillQueryBuilder(skills);
        queryBuilder.withQuery(filteredQuery(skillQueryBuilder,
                boolFilter().must(getRangeFilterBuilder("approvedDate", "now-6M/M", null))
                            .must(getJobIndustriesFilterBuilder(jobCategories))
                            .must(getSalaryRangeFilterBuilder(MIN_SALARY_ACCEPTABLE, MAX_SALARY_ACCEPTABLE))));
        return queryBuilder;
    }

    @Override
    public NativeSearchQueryBuilder getSearchQueryForPriceJobReport(PriceJobEntity priceJobEntity) {
        NativeSearchQueryBuilder queryBuilder = getVietnamworksJobCountQuery();

        QueryBuilder jobTitleQueryBuilder = jobTitleQueryBuilder(priceJobEntity.getJobTitle());
        FilterBuilder locationFilterBuilder = getLocationFilterBuilder(priceJobEntity.getLocationId());
        FilterBuilder jobLevelFilterBuilder = getJobLevelFilterBuilder(priceJobEntity.getJobLevelIds());
        FilterBuilder jobIndustriesFilterBuilder = getJobIndustriesFilterBuilder(priceJobEntity.getJobCategories());
        FilterBuilder approvedDateRangeFilterBuilder = getRangeFilterBuilder("approvedDate", "now-6M/M", null);
        FilterBuilder salaryRangeFilterBuilder = getSalaryRangeFilterBuilder(MIN_SALARY_ACCEPTABLE, MAX_SALARY_ACCEPTABLE);

        queryBuilder.withQuery(filteredQuery(jobTitleQueryBuilder,
                boolFilter().must(locationFilterBuilder)
                            .must(jobLevelFilterBuilder)
                            .must(approvedDateRangeFilterBuilder)
                            .must(jobIndustriesFilterBuilder)
                            .must(salaryRangeFilterBuilder)));
        return queryBuilder;
    }

    @Override
    public NativeSearchQueryBuilder getJobSearchQueryByJobTitle(String jobTitle) {
        NativeSearchQueryBuilder queryBuilder = getVietnamworksJobCountQuery();

        queryBuilder.withQuery(filteredQuery(jobTitleQueryBuilder(jobTitle),
                boolFilter().must(getRangeFilterBuilder("approvedDate", "now-6M/M", null))
                            .must(getSalaryRangeFilterBuilder(MIN_SALARY_ACCEPTABLE, MAX_SALARY_ACCEPTABLE))));
        return queryBuilder;
    }

    private List<String> processSkillsBeforeSearch(List<String> skills) {
        List<String> analyzedSkills = new ArrayList<>();
        for (String skill : skills) {
            analyzedSkills.add(skill.toLowerCase());
            analyzedSkills.add(skill.toUpperCase());
            analyzedSkills.add(StringUtils.capitalize(skill));
        }
        return analyzedSkills;
    }

    @Override
    public NativeSearchQueryBuilder getTopDemandedSkillQueryByJobTitle(String jobTitle, List<Long> jobCategories, Integer jobLevelId) {
        NativeSearchQueryBuilder queryBuilder = getVietnamworksJobCountQuery();

        //pre-process job title in case user enters multiple roles of his job
        List<String> jobTitleTokens = preprocessJobTitle(jobTitle);

        BoolQueryBuilder jobTitleQueryBuilder = boolQuery();
        jobTitleTokens.forEach(jobTitleToken -> jobTitleQueryBuilder.should(jobTitleQueryBuilder(jobTitleToken.trim())));

        BoolFilterBuilder boolFilterBuilder = boolFilter();
        boolFilterBuilder.must(getRangeFilterBuilder("approvedDate", "now-6M/M", null));

        if (jobCategories != null && !jobCategories.isEmpty()) {
            boolFilterBuilder.must(getJobIndustriesFilterBuilder(jobCategories));
        }

        if (jobLevelId != null && jobLevelId > 0) {
            boolFilterBuilder.must(getJobLevelFilterBuilder(Arrays.asList(jobLevelId)));
        }

        queryBuilder.withQuery(filteredQuery(jobTitleQueryBuilder, boolFilterBuilder));
        return queryBuilder;
    }

    @Override
    public NestedBuilder getTopDemandedSkillsAggregation() {
        return nested("top_demanded_skills").path("skills").subAggregation(terms("top_demanded_skills").field("skills.skillName").size(20));
    }

    @Override
    public FilterAggregationBuilder getSalaryAverageAggregation(String fieldName) {
        return filter(fieldName + "_avg").filter(rangeFilter(fieldName).from(250L))
                .subAggregation(avg(fieldName + "_avg").field(fieldName));
    }

    /*
     TODO : This is not a completely sustainable solution, first we should change to see whether the total number of
     no data report would decrease or not. If yes, we will improve it more better later
     */
    private List<String> preprocessJobTitle(String jobTitle) {
        String[] tokens;
        if (jobTitle.contains("&")) {
            tokens = jobTitle.split("&");
        } else if (jobTitle.contains("/")) {
            tokens = jobTitle.split("/");
        } else if (jobTitle.contains(",")) {
            tokens = jobTitle.split(",");
        } else if (jobTitle.contains("và")) {
            tokens = jobTitle.split("và");
        } else if (jobTitle.contains("and")) {
            tokens = jobTitle.split("and");
        } else if (jobTitle.contains("or")) {
            tokens = jobTitle.split("or");
        } else if (jobTitle.contains("hoặc")) {
            tokens = jobTitle.split("hoặc");
        } else {
            tokens = new String[]{jobTitle};
        }
        return Arrays.asList(tokens);
    }
}
