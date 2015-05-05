package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.EmployerEntity;
import com.techlooper.entity.JobOfferInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by phuonghqh on 5/5/15.
 */
@Repository
public interface JobOfferInfoRepository extends ElasticsearchRepository<JobOfferInfo, Long> {
}
