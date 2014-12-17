package com.techlooper.service.impl;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.UserInfo;
import com.techlooper.repository.couchbase.UserRepository;
import com.techlooper.service.UserService;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by NguyenDangKhoa on 12/11/14.
 */
@Service
public class UserServiceImpl implements UserService {

  @Resource
  private UserRepository userRepository;

//  @Resource
//  private Mapper dozerMapper;

  @Resource
  private CouchbaseClient couchbaseClient;

  public void save(UserEntity user) {
    userRepository.save(user);
  }

  public UserEntity findById(String id) {
    return userRepository.findOne(id);
  }

  public UserEntity findByKey(String key) {
    Query query = new Query();
    query.setKey(key);
    query.setLimit(1);
    query.setStale(Stale.FALSE);
    return Optional.ofNullable(userRepository.findByKey(query)).orElse(new UserEntity());
//    return dozerMapper.map(userEntity, UserInfo.class);
  }
}
