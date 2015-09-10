package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.JobEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VietnamworksJobRepository extends ElasticsearchRepository<JobEntity, String> {
}
