package com.techlooper.service;

import com.techlooper.entity.userimport.UserImportEntity;

/**
 * Created by NguyenDangKhoa on 3/19/15.
 */
public interface UserEvaluationService {

    long score(UserImportEntity user);

    long rate(UserImportEntity user);

    long rank(UserImportEntity user);
}
