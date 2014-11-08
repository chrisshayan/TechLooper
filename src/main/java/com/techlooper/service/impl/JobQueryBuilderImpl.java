package com.techlooper.service.impl;

import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobQueryBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * Created by phuonghqh on 11/8/14.
 */
@Component
public class JobQueryBuilderImpl implements JobQueryBuilder {

  public QueryBuilder getTechnicalTermsQuery() {
    final BoolQueryBuilder technicalTermsQuery = QueryBuilders.boolQuery();
    Stream.of(TechnicalTermEnum.values())
      .map(term -> QueryBuilders.multiMatchQuery(term, SEARCH_JOB_FIELDS).operator(MatchQueryBuilder.Operator.AND))
      .forEach(query -> technicalTermsQuery.should(query));
    return technicalTermsQuery;
  }

  public QueryBuilder getTechnicalTermQuery(TechnicalTermEnum term) {
    return QueryBuilders.multiMatchQuery(term, SEARCH_JOB_FIELDS).operator(MatchQueryBuilder.Operator.AND);
  }

  public QueryBuilder getTechnicalSkillQuery(String skill) {
    return QueryBuilders.multiMatchQuery(skill, SEARCH_JOB_FIELDS).operator(MatchQueryBuilder.Operator.AND);
  }

  public NativeSearchQueryBuilder getVietnamworksJobQuery() {
    return new NativeSearchQueryBuilder().withIndices(ES_VIETNAMWORKS_INDEX).withTypes("job");
  }

  public AggregationBuilder getTechnicalTermAggregation(TechnicalTermEnum term) {
    return AggregationBuilders.filter(term.name()).filter(FilterBuilders.queryFilter(this.getTechnicalTermQuery(term)));
  }
}
