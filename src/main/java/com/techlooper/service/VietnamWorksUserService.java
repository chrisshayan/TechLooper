package com.techlooper.service;

import com.techlooper.model.VNWUserInfo;

/**
 * Created by NguyenDangKhoa on 1/16/15.
 */
public interface VietnamWorksUserService {

    boolean existUser(String userEmail);

    boolean register(VNWUserInfo userInfo);
}
