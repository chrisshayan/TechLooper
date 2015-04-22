package com.techlooper.service.impl;

import com.techlooper.entity.Company;
import com.techlooper.entity.CompanyEntity;
import com.techlooper.model.*;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.CompanyService;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import com.techlooper.util.EncryptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.FilterBuilders.andFilter;
import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by chrisshayan on 7/14/14.
 */
@Service
public class VietnamWorksJobStatisticService implements JobStatisticService {

    private static final String ALL_TERMS = "allTerms";

    private static final long LIMIT_NUMBER_OF_COMPANIES = 5;

    private static final long LIMIT_NUMBER_OF_SKILLS = 5;

    private static final long LIMIT_NUMBER_OF_MONTHS = 13;

    private static final double LOWER_BOUND_SALARY = 250;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    @Resource
    private JsonConfigRepository jsonConfigRepository;

    @Resource
    private CompanyService companyService;

    @Value("${elasticsearch.index.name}")
    private String elasticSearchIndexName;

    public Long count(final TechnicalTerm term) {
        final SearchQuery searchQuery = jobQueryBuilder.getVietnamworksJobCountQuery()
                .withFilter(jobQueryBuilder.getTechnicalTermQueryNotExpired(term))
                .build();
        return elasticsearchTemplate.count(searchQuery);
    }

    public SkillStatisticResponse countJobsBySkill(TechnicalTerm term, HistogramEnum... histogramEnums) {
        NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getVietnamworksJobCountQuery();
        queryBuilder.withFilter(jobQueryBuilder.getTechnicalTermsQuery());// all technical terms query

        queryBuilder.addAggregation(getTermsAggregationNotExpired(term));// technical terms agg which has not expired

        AggregationBuilder technicalTermAggregation = jobQueryBuilder.getTechnicalTermAggregation(term);// technical term agg including expired jobs
        getAggregationsSkillNotExpired(term, histogramEnums).stream()
                .forEach(aggs -> aggs.stream().forEach(technicalTermAggregation::subAggregation));
        queryBuilder.addAggregation(technicalTermAggregation);

        return toSkillStatisticResponse(term, elasticsearchTemplate.query(queryBuilder.build(), SearchResponse::getAggregations));
    }

    @Override
    public Long countJobsBySkillWithinPeriod(String skill, HistogramEnum period) {
        final SearchQuery searchQuery = jobQueryBuilder.getVietnamworksJobCountQuery()
                .withFilter(jobQueryBuilder.getTechnicalTermQueryAvailableWithinPeriod(skill, period))
                .build();
        return elasticsearchTemplate.count(searchQuery);
    }

    @Override
    public Long countTotalITJobsWithinPeriod(HistogramEnum period) {
        String lastPeriod = "now-" + period.getPeriod() + period.getUnit();
        final SearchQuery searchQuery = jobQueryBuilder.getVietnamworksJobCountQuery().withQuery(nestedQuery("industries",
                boolQuery()
                        .should(termQuery("industries.industryId", 35))
                        .should(termQuery("industries.industryId", 55))
                        .should(termQuery("industries.industryId", 57))
                        .minimumNumberShouldMatch(1))).withFilter(
                rangeFilter("approvedDate").from(lastPeriod).to("now")).build();

        return elasticsearchTemplate.count(searchQuery);
    }

    @Override
    public Map<String, Double> getAverageSalaryBySkill(TechnicalTerm term) {
        final double LOWER_BOUND_SALARY = 250;
        BoolQueryBuilder termSearchTextQuery = boolQuery();
        term.getSearchTexts().forEach(termSearchText -> termSearchTextQuery.should(matchPhraseQuery("jobTitle", termSearchText)));

        BoolQueryBuilder queryJobBySkill = boolQuery().must(
                termSearchTextQuery).must(rangeQuery("expiredDate").from("now"));
        FilterBuilder filterByAvailableSalary = andFilter(
                rangeFilter("salaryMin").from(LOWER_BOUND_SALARY), rangeFilter("salaryMax").from(LOWER_BOUND_SALARY));
        FilteredQueryBuilder avgSalaryQuery = filteredQuery(queryJobBySkill, filterByAvailableSalary);

        final SearchQuery searchQuery = jobQueryBuilder.getVietnamworksJobCountQuery()
                .withQuery(avgSalaryQuery)
                .addAggregation(AggregationBuilders.avg("avg_salary_min").field("salaryMin"))
                .addAggregation(AggregationBuilders.avg("avg_salary_max").field("salaryMax"))
                .build();

        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        double avgSalaryMin = ((InternalAvg) aggregations.get("avg_salary_min")).getValue();
        double avgSalaryMax = ((InternalAvg) aggregations.get("avg_salary_max")).getValue();

        Map<String, Double> result = processSalaryData(avgSalaryMin, avgSalaryMax);
        return result;
    }

    /**
     * @param term         See more {@link com.techlooper.model.TechnicalTerm}
     * @param aggregations See more {@link org.elasticsearch.search.aggregations.Aggregations}
     * @return Returns an instance of {@link com.techlooper.model.SkillStatisticResponse} which is having detail information for each technical term.
     */
    private SkillStatisticResponse toSkillStatisticResponse(TechnicalTerm term, Aggregations aggregations) {
        final SkillStatisticResponse.Builder skillStatisticResponse =
                new SkillStatisticResponse.Builder().withLabel(term.getLabel());
        InternalFilter allTermsResponse = aggregations.get(ALL_TERMS);
        skillStatisticResponse.withTotalTechnicalJobs(allTermsResponse.getDocCount());
        skillStatisticResponse.withCount(((InternalFilter) allTermsResponse.getAggregations().get(term.getKey())).getDocCount());

        InternalFilter termAggregation = aggregations.get(term.getKey());
        Map<String, List<Long>> skillHistogramsMap = termAggregation.getAggregations().asList().stream().map(agg -> (InternalFilter) agg)
                .sorted((bucket1, bucket2) -> bucket1.getName().compareTo(bucket2.getName()))
                .collect(Collectors.groupingBy(bucket -> bucket.getName().substring(0, bucket.getName().lastIndexOf("-")),
                        Collectors.mapping(InternalFilter::getDocCount, Collectors.toList())));

        List<SkillStatistic> skillStatistics = getSkillStatisticsByName(skillHistogramsMap);
        skillStatistics.stream().forEach(skillStat -> {
            Skill skill = term.getSkillByName(skillStat.getSkillName());
            skillStat.setLogoUrl(skill.getLogoUrl());
            skillStat.setWebSite(skill.getWebSite());
            skillStat.setUsefulLinks(skill.getUsefulLinks());
        });

        return skillStatisticResponse.withSkills(skillStatistics)
                .withLogoUrl(term.getLogoUrl())
                .withWebSite(term.getWebSite())
                .withUsefulLinks(term.getUsefulLinks())
                .build();
    }

    /**
     * Loads data for each skill
     *
     * @param skillHistogramsMap key is skillName like spring and the value list of duration like 7 days or 30 days
     * @return An instance of {@link com.techlooper.model.SkillStatistic}
     */
    private List<SkillStatistic> getSkillStatisticsByName(Map<String, List<Long>> skillHistogramsMap) {
        Map<String, SkillStatistic.Builder> skillStatisticMap = new HashMap<>();
        skillHistogramsMap.forEach((bucketName, docCounts) -> {
            String[] skillNameParts = bucketName.split("-");
            String readableSkillName = EncryptionUtils.decodeHexa(skillNameParts[0]);
            SkillStatistic.Builder skill = Optional.ofNullable(skillStatisticMap.get(readableSkillName)).orElseGet(SkillStatistic.Builder::new);
            skillStatisticMap.put(readableSkillName, skill.withSkillName(readableSkillName));
            skill.withHistogram(new Histogram.Builder().withName(HistogramEnum.valueOf(skillNameParts[1])).withValues(docCounts).build());
        });
        return skillStatisticMap.keySet().stream().map(key -> skillStatisticMap.get(key).build()).collect(Collectors.toList());
    }

    /**
     * Builds a filter to use on Elastic Search
     *
     * @param term See more at {@link com.techlooper.model.TechnicalTerm}
     * @return The builder to be used for filtering the data on ES
     */
    private FilterAggregationBuilder getTermsAggregationNotExpired(TechnicalTerm term) {
        FilterAggregationBuilder aggregation = AggregationBuilders.filter(ALL_TERMS).filter(jobQueryBuilder.getTechnicalTermsQueryNotExpired());
        aggregation.subAggregation(jobQueryBuilder.getTechnicalTermAggregation(term));
        return aggregation;
    }

    /**
     * @param term           See more at {@link com.techlooper.model.TechnicalTerm}
     * @param histogramEnums See more at {@link com.techlooper.model.HistogramEnum}
     * @return List of filters.
     */
    private List<List<FilterAggregationBuilder>> getAggregationsSkillNotExpired(TechnicalTerm term, HistogramEnum... histogramEnums) {
        List<List<FilterAggregationBuilder>> list = new ArrayList<>();
        Arrays.stream(histogramEnums)
                .forEach(histogramEnum -> list.addAll(jobQueryBuilder.toSkillAggregations(term.getSkills(), histogramEnum)));
        return list;
    }

    public TermStatisticResponse generateTermStatistic(TermStatisticRequest term, HistogramEnum histogramEnum) {
        processStatisticRequestParameter(term);

        NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getVietnamworksJobCountQuery();
        QueryBuilder termQueryBuilder = jobQueryBuilder.getTermQueryBuilder(term);

        Integer jobLevelId = term.getJobLevelId();
        if (jobLevelId != null && jobLevelId > 0) {
            FilterBuilder jobLevelFilter = FilterBuilders.termFilter("jobLevelId", jobLevelId);
            // Using filtered query to improve performance
            queryBuilder.withQuery(filteredQuery(termQueryBuilder, jobLevelFilter));
        } else {
            queryBuilder.withQuery(filteredQuery(termQueryBuilder, FilterBuilders.matchAllFilter()));
        }

        FilterAggregationBuilder topCompaniesAggregation = jobQueryBuilder.getTopCompaniesAggregation();
        List<FilterAggregationBuilder> skillAnalyticsAggregations =
                jobQueryBuilder.getSkillAnalyticsAggregations(term, histogramEnum);

        // Add aggregations into the query
        queryBuilder.addAggregation(topCompaniesAggregation);
        skillAnalyticsAggregations.forEach(skillAnalyticsAggregation -> queryBuilder.addAggregation(skillAnalyticsAggregation));

        Aggregations aggregations = elasticsearchTemplate.query(queryBuilder.build(), SearchResponse::getAggregations);
        return transformAggregationsToTermStatisticResponse(term, histogramEnum, aggregations);
    }

    private void processStatisticRequestParameter(TermStatisticRequest term) {
        // When init page we have to popular top 5 skills from JSON configuration by default
        if (term.getSkills() == null || term.getSkills().isEmpty()) {
            term.setSkills(new ArrayList<>());
            TechnicalTerm technicalTerm = jsonConfigRepository.findByKey(term.getTerm());
            if (technicalTerm != null) {
                List<Skill> configuredSkills = technicalTerm.getSkills();
                configuredSkills.stream().forEach(configuredSkill -> term.getSkills().add(configuredSkill.getName()));
            }
        }
    }

    private TermStatisticResponse transformAggregationsToTermStatisticResponse(
            TermStatisticRequest term, HistogramEnum histogramEnum, Aggregations aggregations) {
        TermStatisticResponse termStatisticResponse = new TermStatisticResponse();

        termStatisticResponse.setTerm(term.getTerm());
        termStatisticResponse.setJobLevelId(term.getJobLevelId());

        // Get the total number of jobs for term
        TechnicalTerm configuredTechnicalTerm = jsonConfigRepository.findByKey(term.getTerm());
        long totalJob = count(configuredTechnicalTerm);
        termStatisticResponse.setTotalJob(totalJob);

        // Get the average salary for term
        Map<String, Double> avgSalary = getAverageSalaryBySkill(configuredTechnicalTerm);
        termStatisticResponse.setAverageSalaryMin(avgSalary.get("SALARY_MIN"));
        termStatisticResponse.setAverageSalaryMax(avgSalary.get("SALARY_MAX"));

        // Get list of top companies
        extractTopCompaniesData(aggregations, termStatisticResponse);

        // Get list of skill trends
        extractSkillTrendAnalyticsData(term, histogramEnum, aggregations, termStatisticResponse);

        return termStatisticResponse;
    }

    private void extractTopCompaniesData(Aggregations aggregations, TermStatisticResponse termStatisticResponse) {
        List<Terms.Bucket> topCompanyBuckets = ((LongTerms)
                ((InternalAggregations) ((InternalFilter) aggregations.get("top_companies")).getAggregations())
                        .get("top_companies")).getBuckets();
        if (!topCompanyBuckets.isEmpty()) {
            List<Company> companies = new ArrayList<>();
            int i = 0;
            while (i < topCompanyBuckets.size() && companies.size() < LIMIT_NUMBER_OF_COMPANIES) {
                String companyId = topCompanyBuckets.get(i).getKey();
                CompanyEntity companyEntity = companyService.findById(Long.valueOf(companyId));
                if (companyEntity != null && StringUtils.isNotEmpty(companyEntity.getCompanyLogoURL())) {
                    Company company = new Company();
                    company.setCompanyId(companyId);
                    company.setName(companyEntity.getCompanyName());
                    company.setCompanyLogoURL(companyEntity.getCompanyLogoURL());
                    companies.add(company);
                }
                i++;
            }
            termStatisticResponse.setCompanies(companies);
        }
    }

    private Map<String, Double> processSalaryData(double avgSalaryMin, double avgSalaryMax) {
        Map<String, Double> result = new HashMap<>();
        if (Double.isNaN(avgSalaryMin) && Double.isNaN(avgSalaryMax)) {
            result.put("SALARY_MIN", LOWER_BOUND_SALARY);
            result.put("SALARY_MAX", Double.NaN);
        } else if (!Double.isNaN(avgSalaryMin) && !Double.isNaN(avgSalaryMax)) {
            result.put("SALARY_MIN", Math.ceil(avgSalaryMin));
            result.put("SALARY_MAX", Math.ceil(avgSalaryMax));
        } else {
            if (Double.isNaN(avgSalaryMin)) {
                result.put("SALARY_MIN", Double.NaN);
                result.put("SALARY_MAX", avgSalaryMax);
            } else {
                result.put("SALARY_MIN", avgSalaryMin);
                result.put("SALARY_MAX", Double.NaN);
            }
        }
        return result;
    }

    private void extractSkillTrendAnalyticsData(TermStatisticRequest term, HistogramEnum histogramEnum,
                                                Aggregations aggregations, TermStatisticResponse termStatisticResponse) {
        List<String> trendingSkills = term.getSkills();
        List<SkillStatistic> skillStatistics = new ArrayList<>();
        for (int i = 0; i < trendingSkills.size(); i++) {
            String encodedSkillAgg = EncryptionUtils.encodeHexa(trendingSkills.get(i)) + "_" + histogramEnum + "_analytics";
            DateHistogram skillHistogram = ((DateHistogram) ((InternalAggregations) ((InternalFilter) aggregations.get(encodedSkillAgg)).getAggregations())
                    .get(encodedSkillAgg));
            List<Long> histogramValues = new ArrayList<>();
            skillHistogram.getBuckets().forEach(bucket -> {
                histogramValues.add(bucket.getDocCount());
            });

            // Fill zero for trailing month which contains empty data, this is the limitation of ES histogram
            while (histogramValues.size() < LIMIT_NUMBER_OF_MONTHS) {
                histogramValues.add(0L);
            }

            SkillStatistic skillStatistic = new SkillStatistic();
            skillStatistic.setSkillName(trendingSkills.get(i));
            skillStatistic.setTotalJob(histogramValues.get(histogramValues.size() - 1));
            Histogram histogram = new Histogram();
            histogram.setName(histogramEnum);
            histogram.setValues(histogramValues);
            skillStatistic.setHistograms(Arrays.asList(histogram));
            skillStatistics.add(skillStatistic);
        }
        // Sort list of trending skills by current total number of jobs and limit up to 5 skills per request
        termStatisticResponse.setSkills(sortTrendingSkillsByTotalJobs(skillStatistics));
    }

    private List<SkillStatistic> sortTrendingSkillsByTotalJobs(List<SkillStatistic> skillStatistics) {
        // Basically we consider the last item in the histogram values is the current total jobs (equivalent to "now")
        return skillStatistics.stream().sorted((skillStat1, skillStat2) ->
                skillStat2.getTotalJob().intValue() - skillStat1.getTotalJob().intValue())
                .limit(LIMIT_NUMBER_OF_SKILLS).collect(Collectors.toList());
    }
}
