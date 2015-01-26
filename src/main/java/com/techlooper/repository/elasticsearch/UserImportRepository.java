package com.techlooper.repository.elasticsearch;

import com.techlooper.entity.JobEntity;
import com.techlooper.entity.UserEntity;
import com.techlooper.entity.UserImportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by chrisshayan on 7/11/14.
 */
public interface UserImportRepository extends ElasticsearchRepository<UserImportEntity, String> {
}
