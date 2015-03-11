package com.techlooper.service;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.Talent;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 3/11/15.
 */
public interface TalentSearchDataProcessor {
    List<Talent> process(List<UserImportEntity> users);
}
