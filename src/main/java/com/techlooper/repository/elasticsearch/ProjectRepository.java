package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.ProjectEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends ElasticsearchRepository<ProjectEntity, Long> {
}
