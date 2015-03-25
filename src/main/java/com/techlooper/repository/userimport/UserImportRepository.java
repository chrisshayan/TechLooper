package com.techlooper.repository.userimport;

import com.techlooper.entity.userimport.UserImportEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by khoa-nd on 27/01/15.
 */
@Repository
public interface UserImportRepository extends ElasticsearchRepository<UserImportEntity, String> {

}