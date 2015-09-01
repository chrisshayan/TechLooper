package com.techlooper.repository.userimport;

import com.techlooper.entity.ScrapeJobEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapeJobRepository extends ElasticsearchRepository<ScrapeJobEntity, String> {
}
