package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.ChallengeEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends ElasticsearchRepository<ChallengeEntity, Long> {
}
