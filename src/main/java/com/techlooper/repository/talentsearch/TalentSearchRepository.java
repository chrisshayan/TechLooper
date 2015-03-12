package com.techlooper.repository.talentsearch;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.TalentSearchParam;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 3/12/15.
 */
public interface TalentSearchRepository {

    public List<UserImportEntity> findTalent(TalentSearchParam param);

}
