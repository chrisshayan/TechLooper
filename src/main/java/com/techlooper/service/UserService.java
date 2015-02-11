package com.techlooper.service;

import com.techlooper.entity.UserEntity;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.model.UserInfo;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 12/11/14.
 */
public interface UserService {

  void save(UserEntity userEntity);

  void save(UserInfo userInfo);

  UserEntity findById(String id);

  UserInfo findUserInfoByKey(String key);

  UserEntity findUserEntityByKey(String key);

  boolean verifyVietnamworksAccount(UserEntity userEntity);

  boolean registerVietnamworksAccount(UserInfo userInfo);

  boolean addCrawledUser(UserImportEntity userImportData, SocialProvider socialProvider);

  int addCrawledUserAll(List<UserImportEntity> users, SocialProvider socialProvider);

  UserImportEntity findUserImportByEmail(String email);
}
