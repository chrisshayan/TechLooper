package com.techlooper.service.impl;

import com.techlooper.entity.UserEntity;
import com.techlooper.repository.couchbase.UserRepository;
import com.techlooper.service.UserService;

import javax.annotation.Resource;

/**
 * Created by NguyenDangKhoa on 12/11/14.
 */
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public void save(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public UserEntity findById(String id) {
        return userRepository.findOne(id);
    }
}
