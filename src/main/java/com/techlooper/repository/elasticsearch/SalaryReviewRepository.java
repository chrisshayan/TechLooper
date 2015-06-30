package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.SalaryReviewEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by phuonghqh on 5/5/15.
 */
@Repository
public interface SalaryReviewRepository extends ElasticsearchRepository<SalaryReviewEntity, Long> {
}
