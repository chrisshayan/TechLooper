package com.techlooper.service.impl;

import com.techlooper.model.*;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import com.techlooper.util.EncryptionUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chrisshayan on 7/14/14.
 */
@Service
public class VietnamWorksJobStatisticService implements JobStatisticService {

  private static final String ALL_TERMS = "allTerms";

  @Resource
  private TechnicalSkillEnumMap technicalSkillEnumMap;

  @Resource
  private ElasticsearchTemplate elasticsearchTemplate;

  @Resource
  private JobQueryBuilder jobQueryBuilder;

  @Value("${elasticsearch.index.name}")
  private String elasticSearchIndexName;

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

  public Long count(final TechnicalTermEnum technicalTermEnum) {
    final SearchQuery searchQuery = jobQueryBuilder.getVietnamworksJobCountQuery()
      .withFilter(jobQueryBuilder.getTechnicalTermQueryNotExpired(technicalTermEnum))
      .build();
    return elasticsearchTemplate.count(searchQuery);
  }

  public Long countTechnicalJobs() {
    final SearchQuery searchQuery = jobQueryBuilder.getVietnamworksJobCountQuery()
      .withFilter(jobQueryBuilder.getTechnicalTermsQueryNotExpired())
      .build();
    return elasticsearchTemplate.count(searchQuery);
  }

  public SkillStatisticResponse countJobsBySkill(TechnicalTermEnum term, HistogramEnum... histogramEnums) {
    NativeSearchQueryBuilder queryBuilder = jobQueryBuilder.getVietnamworksJobCountQuery();
    queryBuilder.withFilter(jobQueryBuilder.getTechnicalTermsQuery());// all technical terms query

    queryBuilder.addAggregation(allTermsAggregation(term));// technical terms agg which has expiredDate from now on

    AggregationBuilder technicalTermAggregation = jobQueryBuilder.getTechnicalTermAggregation(term);
    subAggregationSkills(term, technicalTermAggregation, histogramEnums);
    queryBuilder.addAggregation(technicalTermAggregation);// technical term aggregation

    Aggregations aggregations = elasticsearchTemplate.query(queryBuilder.build(), SearchResponse::getAggregations);

    InternalFilter allTermsResponse = aggregations.get(ALL_TERMS);
    final SkillStatisticResponse.Builder skillStatisticResponse = new SkillStatisticResponse.Builder().withJobTerm(term);
    skillStatisticResponse.withTotalTechnicalJobs(allTermsResponse.getDocCount());
    skillStatisticResponse.withCount(((InternalFilter) allTermsResponse.getAggregations().get(term.name())).getDocCount());

    InternalFilter termAggregation = aggregations.get(term.name());
    Map<String, SkillStatistic.Builder> jobSkillsMap = new HashMap<>();
    termAggregation.getAggregations().asList().stream().map(agg -> (InternalFilter) agg)
      .sorted((bucket1, bucket2) -> bucket1.getName().compareTo(bucket2.getName()))
      .collect(Collectors.groupingBy(bucket -> bucket.getName().substring(0, bucket.getName().lastIndexOf("-")),
        Collectors.mapping(InternalFilter::getDocCount, Collectors.toList())))
      .forEach((skillName, docCounts) -> toSkillStatistic(jobSkillsMap, skillName, docCounts));

    List<SkillStatistic> skills = jobSkillsMap.keySet().stream()
      .map(key -> jobSkillsMap.get(key).build()).collect(Collectors.toList());

    return skillStatisticResponse.withSkills(skills).build();
  }

  private void toSkillStatistic(Map<String, SkillStatistic.Builder> jobSkillsMap, String skillName, List<Long> docCounts) {
    String name = EncryptionUtils.decodeHexa(skillName.split("-")[0]);
    SkillStatistic.Builder skill = Optional.ofNullable(jobSkillsMap.get(name)).orElseGet(SkillStatistic.Builder::new);
    jobSkillsMap.put(name, skill.withSkillName(name));
    String histogramName = skillName.split("-")[1];
    skill.withHistogram(new Histogram.Builder().withName(HistogramEnum.valueOf(histogramName)).withValues(docCounts).build());
  }

  private FilterAggregationBuilder allTermsAggregation(TechnicalTermEnum term) {
    FilterAggregationBuilder allTermsAgg = AggregationBuilders.filter(ALL_TERMS).filter(jobQueryBuilder.getTechnicalTermsQueryNotExpired());
    allTermsAgg.subAggregation(jobQueryBuilder.getTechnicalTermAggregation(term));
    return allTermsAgg;
  }

  private void subAggregationSkills(TechnicalTermEnum term, AggregationBuilder technicalTermAggregation, HistogramEnum[] histogramEnums) {
    List<List<FilterAggregationBuilder>> list = new ArrayList<>();
    for (HistogramEnum histogramEnum : histogramEnums) {
      list.addAll(jobQueryBuilder.toSkillAggregations(technicalSkillEnumMap.skillOf(term), histogramEnum));
    }
    list.stream().forEach(aggs -> aggs.stream().forEach(technicalTermAggregation::subAggregation));
  }
}
