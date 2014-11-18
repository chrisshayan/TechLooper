package com.techlooper.service.impl;

import com.techlooper.model.HistogramEnum;
import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.util.EncryptionUtils;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by phuonghqh on 11/8/14.
 */
@Component
public class JobQueryBuilderImpl implements JobQueryBuilder {

  @Value("${elasticsearch.index.name}")
  private String elasticSearchIndexName;

  public FilterBuilder getTechnicalTermsQuery() {
    BoolFilterBuilder boolFilter = FilterBuilders.boolFilter();
    Stream.of(TechnicalTermEnum.values()).map(this::getTechnicalTermQuery).forEach(boolFilter::should);
    return boolFilter;
  }

  public FilterBuilder getTechnicalTermQuery(TechnicalTermEnum term) {
    return FilterBuilders.queryFilter(QueryBuilders.multiMatchQuery(term, SEARCH_JOB_FIELDS).operator(MatchQueryBuilder.Operator.AND));
  }

  public FilterBuilder getTechnicalSkillQuery(String skill) {
    return FilterBuilders.queryFilter(
      QueryBuilders.multiMatchQuery(skill, SEARCH_JOB_FIELDS).operator(MatchQueryBuilder.Operator.AND)).cache(true);
  }

  public NativeSearchQueryBuilder getVietnamworksJobCountQuery() {
    return new NativeSearchQueryBuilder().withIndices(elasticSearchIndexName).withTypes("job").withSearchType(SearchType.COUNT);
  }

  public AggregationBuilder getTechnicalTermAggregation(TechnicalTermEnum term) {
    return AggregationBuilders.filter(term.name()).filter(this.getTechnicalTermQuery(term));
  }

  /**
   * Constructs a query based on a specific period.
   *
   * @param skill         is the detail of term, for example Java is a term and spring is a skill
   * @param skillQuery    {@link org.elasticsearch.index.query.QueryBuilder}
   * @param histogramEnum {@link com.techlooper.model.HistogramEnum}
   * @param total      {@link com.techlooper.model.HistogramEnum#getTotal()}
   * @return {@link org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder}
   */
  private FilterAggregationBuilder getSkillIntervalAggregation(String skill, FilterBuilder skillQuery,
                                                               HistogramEnum histogramEnum, Integer total) {
    String intervalDate = LocalDate.now().minusDays(total).format(DateTimeFormatter.ofPattern("YYYYMMdd"));
    String to = "now-" + (total * histogramEnum.getPeriod()) + histogramEnum.getUnit();
    RangeFilterBuilder approveDateQuery = FilterBuilders.rangeFilter("approvedDate").to(to).cache(true);
    RangeFilterBuilder expiredDateQuery = FilterBuilders.rangeFilter("expiredDate").gte(to).cache(true);
    BoolFilterBuilder filterQuery = FilterBuilders.boolFilter().must(skillQuery, approveDateQuery, expiredDateQuery);
    return AggregationBuilders.filter(EncryptionUtils.encodeHexa(skill) + "-" + histogramEnum + "-" + intervalDate).filter(filterQuery);
  }

  public List<List<FilterAggregationBuilder>> toSkillAggregations(List<String> skills, HistogramEnum histogramEnum) {
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
    return FilterBuilders.rangeFilter("expiredDate").from(from).cache(true);
  }

  public FilterBuilder getTechnicalTermsQueryNotExpired() {
    BoolFilterBuilder allTerms = (BoolFilterBuilder) this.getTechnicalTermsQuery();
    return allTerms.must(this.getExpiredDateQuery("now"));
  }

  public FilterBuilder getTechnicalTermQueryNotExpired(TechnicalTermEnum technicalTermEnum) {
    return FilterBuilders.boolFilter().must(this.getTechnicalTermQuery(technicalTermEnum), this.getExpiredDateQuery("now"));
  }
}
