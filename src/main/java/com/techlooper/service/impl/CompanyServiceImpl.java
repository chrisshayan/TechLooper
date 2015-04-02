package com.techlooper.service.impl;

import com.techlooper.entity.CompanyEntity;
import com.techlooper.repository.userimport.CompanyRepository;
import com.techlooper.service.CompanyService;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by phuonghqh on 4/2/15.
 */
@Service
public class CompanyServiceImpl implements CompanyService {

  @Resource
  private ElasticsearchTemplate elasticsearchTemplateUserImport;

  @Resource
  private CompanyRepository companyRepository;

  @Value("${elasticsearch.userimport.index.name}")
  private String indexName;

  public CompanyEntity findById(Long id) {
    return companyRepository.findOne(id);
  }

  public CompanyEntity findByName(String companyName) {
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(indexName)
      .withTypes("company");//.withPageable();//.withSearchType(SearchType.COUNT);
    queryBuilder.withFilter(FilterBuilders.queryFilter(QueryBuilders.matchPhraseQuery("companyName", companyName)));


    List<CompanyEntity> companies = elasticsearchTemplateUserImport.queryForList(queryBuilder.build(), CompanyEntity.class);
    if (companies.size() > 0) {
      return companies.get(0);
    }
    return null;
  }
}
