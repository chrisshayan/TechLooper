package com.techlooper.service;

import com.techlooper.entity.UserEntity;

/**
 * Created by NguyenDangKhoa on 12/11/14.
 */
public interface UserService {

    void save(UserEntity user);

    UserEntity findById(String id);
}
