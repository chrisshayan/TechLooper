package com.techlooper.service.impl;

import com.techlooper.model.TechnicalSkillEnumMap;
import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobStatisticService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  @Override
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
    } else {
      return 0L;
    }
  }

  public void countJobsBySKill(TechnicalTermEnum termEnum) {
    final List<AggregationBuilder> aggs = new LinkedList<>();
    technicalSkillEnumMap.skillOf(termEnum).stream().map(skill -> {
      String termAndSkill = new StringJoiner(" ").add(termEnum.value()).add(skill).toString();
      QueryBuilder termAndSkillQuery = QueryBuilders.multiMatchQuery(termAndSkill, SEARCH_JOB_FIELDS).operator(Operator.AND);
      QueryBuilder approveDateQuery = QueryBuilders.rangeQuery("approvedDate").to("now");
      BoolQueryBuilder filterQuery = QueryBuilders.boolQuery().must(termAndSkillQuery).must(approveDateQuery);
      return AggregationBuilders.filter(termAndSkill).filter(FilterBuilders.queryFilter(filterQuery));
    }).collect(Collectors.toList());

//    technicalSkillEnumMap.skillOf(termEnum).stream().forEach(skill -> {
//      String termAndSkill = new StringJoiner(" ").add(termEnum.value()).add(skill).toString();
//      QueryBuilder termAndSkillQuery = QueryBuilders.multiMatchQuery(termAndSkill, SEARCH_JOB_FIELDS).operator(Operator.AND);
//      QueryBuilder approveDateQuery = QueryBuilders.rangeQuery("approvedDate").to("now");
//      BoolQueryBuilder filterQuery = QueryBuilders.boolQuery().must(termAndSkillQuery).must(approveDateQuery);
//      aggs.add(AggregationBuilders.filter(termAndSkill).filter(FilterBuilders.queryFilter(filterQuery)));
////      query[0] = new NativeSearchQueryBuilder()
////        .withIndices(ES_VIETNAMWORKS_INDEX).withSearchType(SearchType.COUNT)
////        .addAggregation(AggregationBuilders.filter(termAndSkill).filter(FilterBuilders.queryFilter(filterQuery)));
//    });
  }

}
