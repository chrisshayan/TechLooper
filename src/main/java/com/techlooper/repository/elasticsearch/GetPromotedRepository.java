package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.GetPromotedEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GetPromotedRepository extends ElasticsearchRepository<GetPromotedEntity, Long> {
}
