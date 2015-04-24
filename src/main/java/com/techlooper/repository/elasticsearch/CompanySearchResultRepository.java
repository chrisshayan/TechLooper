package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.EmployerEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CompanySearchResultRepository extends ElasticsearchRepository<EmployerEntity, Long> {
}
