package com.techlooper.service.impl;

import com.techlooper.model.*;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

/**
 * Created by chrisshayan on 7/14/14.
 */
@Service
public class VietnamWorksJobStatisticService implements JobStatisticService {

  private static final String[] SEARCH_JOB_FIELDS = new String[]{"jobTitle", "jobDescription", "skillExperience"};

  private static final String ES_VIETNAMWORKS_INDEX = "vietnamworks";

  @Resource
  private TechnicalSkillEnumMap technicalSkillEnumMap;

  @Resource
  private ElasticsearchTemplate elasticsearchTemplate;

  @Resource
  private JobQueryBuilder jobQueryBuilder;

  public Long countPhpJobs() {
    return count(TechnicalTermEnum.PHP);
  }

  public Long countJavaJobs() {
    return count(TechnicalTermEnum.JAVA);
  }

  public Long countDotNetJobs() {
    return count(TechnicalTermEnum.DOTNET);
  }

  public Long countProjectManagerJobs() {
    return count(TechnicalTermEnum.PROJECT_MANAGER);
  }

  public Long countBAJobs() {
    return count(TechnicalTermEnum.BA);
  }

  public Long countQAJobs() {
    return count(TechnicalTermEnum.QA);
  }

  public Long countDBAJobs() {
    return count(TechnicalTermEnum.QA);
  }

  public Long countPythonJobs() {
    return count(TechnicalTermEnum.PYTHON);
  }

  public Long countRubyJobs() {
    return count(TechnicalTermEnum.RUBY);
  }

  /**
   * Counts the matching jobs to relevant {@code TechnicalTermEnum}
   *
   * @param technicalTermEnum a {@code TechnicalTermEnum} to determine which technology search
   *                          must happen.
   * @return a {@code Long} that represents number of matching jobs.
   */
  public Long count(final TechnicalTermEnum technicalTermEnum) {
    final FilterBuilder isActiveJobFilter = FilterBuilders.termFilter("isActive", 1);
    final SearchQuery searchQuery = new NativeSearchQueryBuilder()
      .withQuery(filteredQuery(
        multiMatchQuery(technicalTermEnum, SEARCH_JOB_FIELDS).operator(Operator.AND),
        isActiveJobFilter))
      .withIndices(ES_VIETNAMWORKS_INDEX).withSearchType(SearchType.COUNT).build();
    return elasticsearchTemplate.count(searchQuery);
  }

  public Long countTechnicalJobs() {
    return Stream.of(TechnicalTermEnum.values()).mapToLong(this::count).sum();
  }

  /**
   * Counting number of jobs by technical term and its skill
   *
   * @param technicalTermEnum
   * @param skill
   * @param approvedDate
   * @return number of jobs
   * @see com.techlooper.model.TechnicalTermEnum
   */
  public Long countTechnicalJobsBySkill(TechnicalTermEnum technicalTermEnum, String skill, LocalDate approvedDate) {
    final Optional term = Optional.ofNullable(technicalTermEnum);

    if (term.isPresent()) {
      final String searchCriteria = StringUtils.join(Arrays.asList(term.get(), skill), ' ').trim();
      final QueryBuilder skillSearchQuery = multiMatchQuery(searchCriteria, SEARCH_JOB_FIELDS)
        .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
        .operator(Operator.AND);
      final FilterBuilder periodQueryFilter = FilterBuilders.rangeFilter("approvedDate").lte(approvedDate.toString());
      final FilterBuilder isActiveJobFilter = FilterBuilders.termFilter("isActive", 1);

      FilterBuilder filterBuilder = periodQueryFilter;
      if (approvedDate.isEqual(LocalDate.now())) {
        filterBuilder = FilterBuilders.andFilter(periodQueryFilter, isActiveJobFilter);
      }
      final SearchQuery searchQuery = new NativeSearchQueryBuilder()
        .withQuery(filteredQuery(skillSearchQuery, filterBuilder))
        .withIndices(ES_VIETNAMWORKS_INDEX)
        .withSearchType(SearchType.COUNT).build();
      return elasticsearchTemplate.count(searchQuery);
    }
    else {
      return 0L;
    }
  }


  // TODO extract common query to reuse
  public SkillStatisticResponse countJobsBySkill(TechnicalTermEnum term, final PeriodEnum period) {
    NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getVietnamworksJobQuery();
    queryBuilder.withQuery(jobQueryBuilder.getTechnicalTermsQuery()).withSearchType(SearchType.COUNT);// all technical terms query
    AggregationBuilder technicalTermAggregation = jobQueryBuilder.getTechnicalTermAggregation(term);
    queryBuilder.addAggregation(technicalTermAggregation);// technical term aggregation

    List<List<FilterAggregationBuilder>> skillAggregations = technicalSkillEnumMap.skillOf(term).stream().parallel().map(skill -> {
      QueryBuilder skillQuery = jobQueryBuilder.getTechnicalSkillQuery(skill);

      // TODO * refactor loop n times using jdk8 later
      //      * consider to extract 30 to properties file ?
      List<FilterAggregationBuilder> builders = new LinkedList<>();
      for (int i = 0; i < 30; ++i) {// 30 days
        builders.add(getIntervalAggregation(skill, skillQuery, "d", i));
      }

      builders.add(getIntervalAggregation(skill, skillQuery, "w", 0));// current week
      builders.add(getIntervalAggregation(skill, skillQuery, "w", 1));// previous week
      return builders;
    }).collect(toList());

    skillAggregations.stream().forEach(aggs -> aggs.stream().forEach(agg -> technicalTermAggregation.subAggregation(agg)));

    final SkillStatisticResponse.Builder skillStatisticResponse = new SkillStatisticResponse.Builder().withJobTerm(term);
    Aggregations aggregations = elasticsearchTemplate.query(queryBuilder.build(), new ResultsExtractor<Aggregations>() {
      public Aggregations extract(SearchResponse response) {
        skillStatisticResponse.withTotalTechnicalJobs(response.getHits().getTotalHits());
        return response.getAggregations();
      }
    });

    //term aggregation
    InternalFilter termAggregation = (InternalFilter) aggregations.get(term.name());
    skillStatisticResponse.withCount(((InternalFilter) aggregations.get(term.name())).getDocCount());

    Function<InternalFilter, String> getSkillNameFunc = bucket -> bucket.getName().split("-")[0];
    Function<InternalFilter, Long> mapToSkillStatisticResponseFunc = bucket -> bucket.getDocCount();
    Collector<InternalFilter, ?, List<Long>> skillCountCollector = mapping(mapToSkillStatisticResponseFunc, toList());

    final List<SkillStatisticItem> jobSkills = new LinkedList<>();
    termAggregation.getAggregations().asList().stream().parallel().map(agg -> (InternalFilter) agg)
      .sorted((bucket1, bucket2) -> bucket1.getName().compareTo(bucket2.getName()))
      .collect(Collectors.groupingBy(getSkillNameFunc, skillCountCollector))
      .forEach((skillName, docCounts) -> {
        jobSkills.add(new SkillStatisticItem.Builder()
          .withSkill(skillName.replaceAll("_", " "))
          .withHistogramData(docCounts)
          .withCurrentCount(docCounts.get(30))
          .withPreviousCount(docCounts.get(31))
          .build());
      });

    return skillStatisticResponse.withJobSkills(jobSkills).build();
  }

  private FilterAggregationBuilder getIntervalAggregation(String skill, QueryBuilder skillQuery, String period, Integer interval) {
    String intervalDate = LocalDate.now().minusDays(interval).format(DateTimeFormatter.ofPattern("YYYYMMdd"));
    QueryBuilder approveDateQuery = QueryBuilders.rangeQuery("approvedDate").to("now-" + interval + period);
    BoolQueryBuilder filterQuery = QueryBuilders.boolQuery().must(skillQuery).must(approveDateQuery);
    return AggregationBuilders.filter(skill.replaceAll(" ", "_") + "-" + period + "-" + intervalDate).filter(FilterBuilders.queryFilter(filterQuery));
  }

}
