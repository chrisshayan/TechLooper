package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.JobEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VietnamworksJobRepository extends ElasticsearchRepository<JobEntity, String> {
}