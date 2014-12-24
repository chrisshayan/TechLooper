package com.techlooper.service;

import com.techlooper.entity.UserEntity;
import com.techlooper.model.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by NguyenDangKhoa on 12/11/14.
 */
public interface UserService {

  void save(UserEntity userEntity);

  void save(UserInfo userInfo);

  UserEntity findById(String id);

  UserInfo findUserInfoByKey(String key);
}
