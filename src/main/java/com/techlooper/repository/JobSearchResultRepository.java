package com.techlooper.repository;

import com.techlooper.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by chrisshayan on 7/11/14.
 */
public interface JobSearchResultRepository extends ElasticsearchRepository<Job, String> {
    Page<Job> findById(final String id, final Pageable pageable);
}
