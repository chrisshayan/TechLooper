package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.ProjectRegistrantEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRegistrantRepository extends ElasticsearchRepository<ProjectRegistrantEntity, Long> {
}
