package com.techlooper.service;

import com.techlooper.entity.UserEntity;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.entity.vnw.dto.VnwUserDto;
import com.techlooper.model.*;

import javax.mail.MessagingException;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 12/11/14.
 */
public interface UserService {

//  void save(UserEntity userEntity);

//  void save(UserInfo userInfo);

//  UserEntity findById(String id);

//  UserInfo findUserInfoByKey(String key);

//  UserEntity findUserEntityByKey(String key);

//  boolean verifyVietnamworksAccount(UserEntity userEntity);

//    boolean registerVietnamworksAccount(UserInfo userInfo);

  /**
   * Adds the crawled user into ElasticSearch
   *
   * @param userImportData the entity
   * @param socialProvider the social
   * @return true if if was successful
   */
  boolean addCrawledUser(UserImportEntity userImportData, SocialProvider socialProvider);

  /**
   * Adds the crawled user into ElasticSearch
   *
   * @param users          list of users
   * @param socialProvider the social
   * @return number of successful saved users
   */
  int addCrawledUserAll(List<UserImportEntity> users, SocialProvider socialProvider, UpdateModeEnum updateMode);

  /**
   * Find the user on ElasticSearch
   * See more at {@linkplain com.techlooper.repository.userimport.UserImportRepository}
   *
   * @param email the email of user
   * @return the imported user into ElasticSearch if there is any match.
   */
  UserImportEntity findUserImportByEmail(String email);

  /**
   * Find users
   *
   * @param pageNumber the page number to fetch users
   * @param pageSize   the size of each page
   * @return list of {@linkplain com.techlooper.entity.userimport.UserImportEntity}
   */
  List<UserImportEntity> getAll(final int pageNumber, final int pageSize);

  /**
   * Find talent users
   *
   * @param param search criteria
   * @return list of {@linkplain com.techlooper.model.TalentSearchResponse}
   */
  TalentSearchResponse findTalent(TalentSearchRequest param);

//  void registerUser(UserInfo userInfo);
//
//  long countRegisteredUser();

  SalaryReviewDto findSalaryReviewById(String base64Id);

  VnwUserDto findVnwUserByUsername(String username);

  boolean sendOnBoardingEmail(String email, Language language) throws MessagingException;
}
