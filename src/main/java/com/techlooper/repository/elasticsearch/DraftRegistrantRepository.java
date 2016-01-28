package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.DraftRegistrantEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by phuonghqh on 1/26/16.
 */
@Repository
public interface DraftRegistrantRepository extends ElasticsearchRepository<DraftRegistrantEntity, Long> {
}
