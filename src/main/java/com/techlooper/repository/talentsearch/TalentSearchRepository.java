package com.techlooper.repository.talentsearch;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.TalentSearchRequest;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 3/12/15.
 */
public interface TalentSearchRepository {

    public List<UserImportEntity> findTalent(TalentSearchRequest param);

}
