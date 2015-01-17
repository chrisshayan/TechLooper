package com.techlooper.service.impl;

import com.techlooper.entity.UserEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.model.UserInfo;
import com.techlooper.model.VNWUserInfo;
import com.techlooper.repository.couchbase.UserRepository;
import com.techlooper.service.UserService;
import org.dozer.Mapper;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by NguyenDangKhoa on 12/11/14.
 */
@Service
public class UserServiceImpl implements UserService {

  @Resource
  private UserRepository userRepository;

  @Resource
  private Mapper dozerMapper;

  @Resource
  private TextEncryptor textEncryptor;

  @Resource
  private VietnamworksUserService vietnamworksUserService;

  public void save(UserEntity userEntity) {
    userRepository.save(userEntity);
  }

  public void save(UserInfo userInfo) {
    UserEntity userEntity = userRepository.findOne(userInfo.getId());
    dozerMapper.map(userInfo, userEntity);
    userRepository.save(userEntity);
  }

  public UserEntity findById(String id) {
    return userRepository.findOne(id);
  }

  public UserInfo findUserInfoByKey(String key) {
    return dozerMapper.map(findUserEntityByKey(key), UserInfo.class);
  }

  public UserEntity findUserEntityByKey(String key) {
    String emailAddress = textEncryptor.decrypt(key);
    return userRepository.findOne(emailAddress);
  }

  public boolean verifyVietnamworksAccount(UserEntity userEntity) {
    boolean result = vietnamworksUserService.existUser(userEntity.getEmailAddress());
    if (result) {
      userEntity.getProfiles().put(SocialProvider.VIETNAMWORKS, null);
    }
    return result;
  }

  public boolean registerVietnamworksAccount(UserInfo userInfo) {
    boolean userAgreeRegister = userInfo.getProfileNames().contains(SocialProvider.VIETNAMWORKS);
    boolean registerSuccess = false;
    if (userAgreeRegister) {
      registerSuccess = vietnamworksUserService.register(
        new VNWUserInfo(userInfo.getEmailAddress(), userInfo.getFirstName(), userInfo.getLastName()));
    }
    if (!registerSuccess) {
      userInfo.getProfileNames().remove(SocialProvider.VIETNAMWORKS);
    }
    return registerSuccess;
  }
}
