package com.techlooper.service;

import com.techlooper.entity.VnwUserProfile;

/**
 * Created by NguyenDangKhoa on 1/16/15.
 */
public interface VietnamworksUserService {

    boolean existUser(String userEmail);

    boolean register(VnwUserProfile userProfile);
}
