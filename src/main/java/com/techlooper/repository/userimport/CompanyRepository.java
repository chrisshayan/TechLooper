package com.techlooper.repository.userimport;

import com.techlooper.entity.CompanyEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by phuonghqh on 4/2/15.
 */
@Repository
public interface CompanyRepository extends ElasticsearchRepository<CompanyEntity, Long> {
}
