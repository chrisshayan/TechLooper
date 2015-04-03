package com.techlooper.service.impl;

import com.techlooper.entity.CompanyEntity;
import com.techlooper.entity.CompanyJob;
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
import java.util.stream.Collectors;

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

    CompanyEntity company = null;
    List<CompanyEntity> companies = elasticsearchTemplateUserImport.queryForList(queryBuilder.build(), CompanyEntity.class);
    if (companies.size() > 0) {
      company = companies.get(0);
      List<CompanyJob> topJobs = company.getJobs().stream().sorted((job1, job2) -> {
        if (job1.getExpiredDate() == null || job2.getExpiredDate() == null) {
          return -1;
        }
        return -1 * job1.getExpiredDate().compareTo(job2.getExpiredDate());
      })
        .limit(10).collect(Collectors.toList());
      company.setJobs(topJobs);
    }
    return company;
  }
}
