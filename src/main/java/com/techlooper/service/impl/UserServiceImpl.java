package com.techlooper.service.impl;

import com.techlooper.entity.SimpleUserProfile;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.model.UserInfo;
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

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public void save(UserInfo userInfo, boolean registerVietnamworks) {
        UserEntity userEntity = userRepository.findOne(userInfo.getId());
        dozerMapper.map(userInfo, userEntity);
        if (registerVietnamworks) {
            userEntity.getProfiles().put(SocialProvider.VIETNAMWORKS, new SimpleUserProfile());
        }
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
}
