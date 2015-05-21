package com.techlooper.service.impl;

import com.techlooper.entity.Company;
import com.techlooper.entity.EmployerEntity;
import com.techlooper.model.*;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.CompanyService;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import com.techlooper.util.EncryptionUtils;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.joda.time.DateTime;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
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

  private static final int LIMIT_NUMBER_OF_MONTHS = 12;

  private static final int EXPERIENCED_LEVEL_ID = 5;

  private static final int MANAGER_LEVEL_ID = 7;

  private static final int DIRECTOR_LEVEL_ID = 3;

  public static final double LOWER_BOUND_SALARY_ENTRY_LEVEL = 250D;

  public static final double LOWER_BOUND_SALARY_EXPERIENCED_LEVEL = 500D;

  public static final double LOWER_BOUND_SALARY_MANAGER_LEVEL = 1000D;

  public static final double LOWER_BOUND_SALARY_DIRECTOR_LEVEL = 2000D;

  @Resource
  private RestTemplate restTemplate;

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

  @Value("${vnw.api.users.createJobAlert.url}")
  private String vnwCreateJobAlertUrl;


  @Value("${vnw.api.key.name}")
  private String vnwApiKeyName;

  @Value("${vnw.api.key.value}")
  private String vnwApiKeyValue;

  @Resource
  private Mapper dozerMapper;

  @Resource
  private JobSearchAPIConfigurationRepository apiConfiguration;

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
  public Map<String, Double> getAverageSalaryBySkill(TechnicalTerm term, List<Integer> jobLevelIds) {
    final double LOWER_BOUND_SALARY = 250;

    // Build the term query
    BoolQueryBuilder termSearchTextQuery = boolQuery();
    term.getSearchTexts().forEach(termSearchText -> termSearchTextQuery.should(matchPhraseQuery("jobTitle", termSearchText)));

    // Build the list of filters
    BoolFilterBuilder availableJobByLevelFilter = boolFilter();
    // Using 'now/d' to leverage filter cache for performance improvement
    availableJobByLevelFilter.must(rangeFilter("expiredDate").from("now/d"));

    if (jobLevelIds != null && !jobLevelIds.isEmpty()) {
      availableJobByLevelFilter.must(jobQueryBuilder.getJobLevelFilterBuilder(jobLevelIds));
    }
    availableJobByLevelFilter.must(rangeFilter("salaryMin").from(LOWER_BOUND_SALARY));
    availableJobByLevelFilter.must(rangeFilter("salaryMax").from(LOWER_BOUND_SALARY));

    FilteredQueryBuilder avgSalaryQuery = filteredQuery(termSearchTextQuery, availableJobByLevelFilter);

    final SearchQuery searchQuery = jobQueryBuilder.getVietnamworksJobCountQuery()
      .withQuery(avgSalaryQuery)
      .addAggregation(AggregationBuilders.avg("avg_salary_min").field("salaryMin"))
      .addAggregation(AggregationBuilders.avg("avg_salary_max").field("salaryMax"))
      .build();

    Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
    double avgSalaryMin = ((InternalAvg) aggregations.get("avg_salary_min")).getValue();
    double avgSalaryMax = ((InternalAvg) aggregations.get("avg_salary_max")).getValue();

    Map<String, Double> result = processSalaryData(avgSalaryMin, avgSalaryMax, jobLevelIds);
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

    List<Integer> jobLevelIds = term.getJobLevelIds();
    if (jobLevelIds != null && !jobLevelIds.isEmpty()) {
      queryBuilder.withQuery(filteredQuery(termQueryBuilder, jobQueryBuilder.getJobLevelFilterBuilder(jobLevelIds)));
    }
    else {
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

    // Get the total number of jobs for term
    TechnicalTerm configuredTechnicalTerm = jsonConfigRepository.findByKey(term.getTerm());
    long totalJob = count(configuredTechnicalTerm);
    termStatisticResponse.setTotalJob(totalJob);

    // Get the average salary for term
    Map<String, Double> avgSalary = getAverageSalaryBySkill(configuredTechnicalTerm, term.getJobLevelIds());
    termStatisticResponse.setAverageSalaryMin(avgSalary.get("SALARY_MIN"));
    termStatisticResponse.setAverageSalaryMax(avgSalary.get("SALARY_MAX"));

    // Get list of top companies
    extractTopCompaniesData(aggregations, termStatisticResponse);

    // Get list of skill trends analytics
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
        EmployerEntity employerEntity = companyService.findEmployerByCompanyId(Long.valueOf(companyId));
        if (employerEntity != null && StringUtils.isNotEmpty(employerEntity.getCompanyLogoURL())) {
          Company company = new Company();
          company.setCompanyId(companyId);
          company.setName(employerEntity.getCompanyName());
          company.setCompanyLogoURL(employerEntity.getCompanyLogoURL());
          List<Long> employerIds = employerEntity.getEmployers().stream().filter(employer -> employer.getIsActive() == 1)
            .map(employer -> employer.getUserId()).collect(Collectors.toList());
          company.setEmployerIds(employerIds);
          companies.add(company);
        }
        i++;
      }
      termStatisticResponse.setCompanies(companies);
    }
  }

  private Map<String, Double> processSalaryData(double avgSalaryMin, double avgSalaryMax, List<Integer> jobLevelIds) {
    Map<String, Double> result = new HashMap<>();
    if (Double.isNaN(avgSalaryMin) && Double.isNaN(avgSalaryMax)) {
      result.put("SALARY_MIN", getLowerBoundSalaryByJobLevel(jobLevelIds));
      result.put("SALARY_MAX", Double.NaN);
    }
    else if (!Double.isNaN(avgSalaryMin) && !Double.isNaN(avgSalaryMax)) {
      result.put("SALARY_MIN", Math.ceil(avgSalaryMin));
      result.put("SALARY_MAX", Math.ceil(avgSalaryMax));
    }
    else {
      if (Double.isNaN(avgSalaryMin)) {
        result.put("SALARY_MIN", Double.NaN);
        result.put("SALARY_MAX", avgSalaryMax);
      }
      else {
        result.put("SALARY_MIN", avgSalaryMin);
        result.put("SALARY_MAX", Double.NaN);
      }
    }
    return result;
  }

  private Double getLowerBoundSalaryByJobLevel(List<Integer> jobLevelIds) {
    if (jobLevelIds.contains(EXPERIENCED_LEVEL_ID)) {
      return LOWER_BOUND_SALARY_EXPERIENCED_LEVEL;
    }
    else if (jobLevelIds.contains(MANAGER_LEVEL_ID)) {
      return LOWER_BOUND_SALARY_MANAGER_LEVEL;
    }
    else if (jobLevelIds.contains(DIRECTOR_LEVEL_ID)) {
      return LOWER_BOUND_SALARY_DIRECTOR_LEVEL;
    }
    return LOWER_BOUND_SALARY_ENTRY_LEVEL;
  }

  private void extractSkillTrendAnalyticsData(TermStatisticRequest term, HistogramEnum histogramEnum,
                                              Aggregations aggregations, TermStatisticResponse termStatisticResponse) {
    List<String> trendingSkills = term.getSkills();
    List<SkillStatistic> skillStatistics = new ArrayList<>();
    for (int i = 0; i < trendingSkills.size(); i++) {
      String encodedSkillAgg = EncryptionUtils.encodeHexa(trendingSkills.get(i)) + "_" + histogramEnum + "_analytics";
      DateHistogram skillHistogram = ((DateHistogram) ((InternalAggregations) ((InternalFilter) aggregations.get(encodedSkillAgg)).getAggregations())
        .get(encodedSkillAgg));

      List<Long> histogramValues = processSkillHistogramValues(skillHistogram);

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

  // Fill zero for trailing month which contains empty data, this is the limitation of ES histogram
  private List<Long> processSkillHistogramValues(DateHistogram skillHistogram) {
    List<Long> histogramValues = new ArrayList<>();
    List<? extends DateHistogram.Bucket> buckets = skillHistogram.getBuckets();

    if (buckets.isEmpty()) {
      return Collections.nCopies(LIMIT_NUMBER_OF_MONTHS, 0L);
    }

    // Start from the point of last year, month over month
    DateTime loopDateTime = DateTime.now().minusMonths(LIMIT_NUMBER_OF_MONTHS);
    int i = 0;
    while (histogramValues.size() < LIMIT_NUMBER_OF_MONTHS) {
      if (i < buckets.size()) {
        DateHistogram.Bucket bucket = buckets.get(i);
        DateTime bucketDateTime = bucket.getKeyAsDate();
        if (bucketDateTime.getYear() == loopDateTime.getYear() &&
          bucketDateTime.getMonthOfYear() == loopDateTime.getMonthOfYear()) {
          histogramValues.add(bucket.getDocCount());
          i++;
        }
        else {
          histogramValues.add(0L);
        }
      }
      else {
        histogramValues.add(0L);
      }
      loopDateTime = loopDateTime.plusMonths(1);
    }

    return histogramValues;
  }

  private List<SkillStatistic> sortTrendingSkillsByTotalJobs(List<SkillStatistic> skillStatistics) {
    // Basically we consider the last item in the histogram values is the current total jobs (equivalent to "now")
    return skillStatistics.stream().sorted((skillStat1, skillStat2) ->
      skillStat2.getTotalJob().intValue() - skillStat1.getTotalJob().intValue())
      .limit(LIMIT_NUMBER_OF_SKILLS).collect(Collectors.toList());
  }

  public void createVnwJobAlert(VnwJobAlertRequest vnwJobAlertRequest) {
    VnwJobAlert jobAlert = dozerMapper.map(vnwJobAlertRequest, VnwJobAlert.class);
    String params = JsonUtils.toJSON(jobAlert).orElse(EMPTY);
    HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(
      MediaType.APPLICATION_JSON,  apiConfiguration.getApiKeyName(), apiConfiguration.getApiKeyValue(), params);
    restTemplate.exchange(vnwCreateJobAlertUrl, HttpMethod.POST, requestEntity, String.class);
  }
}
