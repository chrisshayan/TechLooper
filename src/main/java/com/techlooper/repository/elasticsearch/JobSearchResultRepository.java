package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.JobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by chrisshayan on 7/11/14.
 */
public interface JobSearchResultRepository extends ElasticsearchRepository<JobEntity, String> {
    Page<JobEntity> findById(final String id, final Pageable pageable);
}
