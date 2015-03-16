package com.techlooper.repository.talentsearch;

import com.techlooper.entity.userimport.UserImportEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by NguyenDangKhoa on 3/16/15.
 */
public interface GITHUBTalentSearchRepository extends ElasticsearchRepository<UserImportEntity,String> {
}
