package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.ChallengeRegistrantEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRegistrantRepository extends ElasticsearchRepository<ChallengeRegistrantEntity, String> {
}
