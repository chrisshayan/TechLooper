package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.ChallengeSubmissionEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeSubmissionRepository extends ElasticsearchRepository<ChallengeSubmissionEntity, Long> {
}
