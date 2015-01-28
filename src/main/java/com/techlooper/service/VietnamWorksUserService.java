package com.techlooper.service;

import com.techlooper.entity.VnwUserProfile;

/**
 * Created by NguyenDangKhoa on 1/16/15.
 */
public interface VietnamWorksUserService {

    boolean existUser(String userEmail);

    boolean register(VnwUserProfile userProfile);
}
