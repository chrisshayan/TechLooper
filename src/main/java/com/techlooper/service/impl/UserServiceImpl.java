package com.techlooper.service.impl;

import com.techlooper.entity.UserEntity;
import com.techlooper.entity.UserImportEntity;
import com.techlooper.entity.VnwUserProfile;
import com.techlooper.model.SocialProvider;
import com.techlooper.model.UserImportData;
import com.techlooper.model.UserInfo;
import com.techlooper.repository.couchbase.UserRepository;
import com.techlooper.repository.elasticsearch.UserImportRepository;
import com.techlooper.service.UserService;
import com.techlooper.service.VietnamworksUserService;
import org.dozer.Mapper;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryFilterBuilder;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;

/**
 * Created by NguyenDangKhoa on 12/11/14.
 */
@Service
public class UserServiceImpl implements UserService {

  @Resource
  private UserRepository userRepository;

  @Resource
  private UserImportRepository userImportRepository;

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
    boolean registerSuccess = false;
    if (userInfo.acceptRegisterVietnamworksAccount() &&
            !(registerSuccess = vietnamworksUserService.register(dozerMapper.map(userInfo, VnwUserProfile.class)))) {
      userInfo.removeProfile(SocialProvider.VIETNAMWORKS);
    }
    return registerSuccess;
  }

  public boolean addCrawledUser(UserImportData userImportData) {
    UserImportEntity userImportEntity = findUserImportByEmail(userImportData.getEmail());

    if (userImportEntity != null) {
      userImportEntity.getProfiles().put(userImportData.getCrawlerSource(), userImportData.getProfile());
    } else {
      userImportEntity = new UserImportEntity();
      UserImportEntity.UserImportEntityBuilder userImportEntityBuilder =
              UserImportEntity.UserImportEntityBuilder.userImportEntity(userImportEntity);
      userImportEntityBuilder.withEmail(userImportData.getEmail());
      userImportEntityBuilder.withFullName(userImportData.getFullName());
      userImportEntityBuilder.withProfile(userImportData.getCrawlerSource(), userImportData.getProfile());
      userImportEntityBuilder.withIsCrawled(true);
    }

    return userImportRepository.save(userImportEntity) != null;
  }

  public UserImportEntity findUserImportByEmail(String email) {
    UserImportEntity userImportEntity = userImportRepository.findOne(email);

    if (userImportEntity != null) {
      return userImportEntity;
    } else {
      QueryFilterBuilder queryFilterBuilder = FilterBuilders.queryFilter(
              QueryBuilders.queryString(email).defaultOperator(Operator.AND)).cache(true);
      SearchQuery userSearchQuery = new NativeSearchQueryBuilder()
              .withFilter(queryFilterBuilder)
              .withPageable(new PageRequest(0, 10))
              .build();
      Page<UserImportEntity> result = userImportRepository.search(userSearchQuery);
      if (result.getNumberOfElements() > 0) {
        return result.getContent().get(0);
      } else {
        return null;
      }
    }
  }
}
