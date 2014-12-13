package com.techlooper.service.impl;

import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.UserInfo;
import com.techlooper.repository.couchbase.UserRepository;
import com.techlooper.service.UserService;
import org.dozer.Mapper;
import org.jasypt.util.password.PasswordEncryptor;
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

  public void save(UserEntity user) {
    userRepository.save(user);
  }

  public UserEntity findById(String id) {
    return userRepository.findOne(id);
  }

  public UserInfo findByKey(String key) {
    Query query = new Query();
    query.setKey(key);
    UserEntity userEntity = userRepository.findByKey(query);
    return dozerMapper.map(userEntity, UserInfo.class);
  }
}
