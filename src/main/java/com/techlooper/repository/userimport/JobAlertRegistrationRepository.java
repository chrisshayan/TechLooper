package com.techlooper.repository.userimport;

import com.techlooper.entity.JobAlertRegistrationEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobAlertRegistrationRepository extends ElasticsearchRepository<JobAlertRegistrationEntity, Long> {
}
