package com.techlooper.service.impl;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.Query;
import com.couchbase.client.protocol.views.Stale;
import com.techlooper.entity.UserEntity;
import com.techlooper.repository.couchbase.UserRepository;
import com.techlooper.service.UserService;
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

  public void save(UserEntity user) {
    userRepository.save(user);

    // TODO: make sure couchbase find by view can work later, remove it later
    Query query = new Query();
    query.setKey(user.getKey());
    query.setLimit(1);
    query.setStale(Stale.FALSE);
    userRepository.findByKey(query);
  }

  public UserEntity findById(String id) {
    return userRepository.findOne(id);
  }

  public UserEntity findByKey(String key) {
    Query query = new Query();
    query.setKey(key);
    query.setLimit(1);
    return Optional.ofNullable(userRepository.findByKey(query)).orElse(new UserEntity());
  }
}
