package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.PriceJobEntity;
import com.techlooper.entity.SalaryReview;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by khoand on 22/5/15.
 */
@Repository
public interface PriceJobReportRepository extends ElasticsearchRepository<PriceJobEntity, Long> {
}
