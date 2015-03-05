package com.techlooper.service;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.UserImportData;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 1/30/15.
 */
public interface UserImportDataProcessor {
    List<UserImportEntity> process(List<UserImportData> users);
}
