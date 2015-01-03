package com.techlooper.service.impl;

import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.UserInfo;
import com.techlooper.repository.couchbase.UserRepository;
import com.techlooper.service.UserService;
import org.dozer.Mapper;
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

  public void save(UserEntity userEntity) {
    userRepository.save(userEntity);

    // TODO: make sure couchbase find by view can work later, remove it later
    Query query = new Query();
    query.setKey(userEntity.getKey());
    query.setLimit(1);
    query.setStale(Stale.FALSE);
    userRepository.findByKey(query);
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
    return dozerMapper.map(findUserEntityByKey(key),  UserInfo.class);
  }

  public UserEntity findUserEntityByKey(String key) {
    Query query = new Query();
    query.setKey(key);
    query.setLimit(1);
    return userRepository.findByKey(query);
  }
}
