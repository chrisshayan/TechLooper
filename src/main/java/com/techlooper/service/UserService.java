package com.techlooper.service;

import com.techlooper.entity.UserEntity;
import com.techlooper.model.UserInfo;

/**
 * Created by NguyenDangKhoa on 12/11/14.
 */
public interface UserService {

  void save(UserEntity user);

  UserEntity findById(String id);

  UserInfo findByKey(String key);
}
