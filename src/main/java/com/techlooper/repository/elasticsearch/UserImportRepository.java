package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.UserImportEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by khoa-nd on 27/01/15.
 */
public interface UserImportRepository extends ElasticsearchRepository<UserImportEntity, String> {

}