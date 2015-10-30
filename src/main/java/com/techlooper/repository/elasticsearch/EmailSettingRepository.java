package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.EmailSettingEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailSettingRepository extends ElasticsearchRepository<EmailSettingEntity, String> {
}
