package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.EmailTemplateEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplateRepository extends ElasticsearchRepository<EmailTemplateEntity, Long> {
}
