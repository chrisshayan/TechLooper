package com.techlooper.service;

import com.techlooper.entity.UserEntity;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by NguyenDangKhoa on 12/11/14.
 */
public interface UserService {

    /**
     * Save the signed user
     * See more {@linkplain com.techlooper.repository.couchbase.UserRepository}
     *
     * @param userEntity user
     */
    void save(UserEntity userEntity);

    /**
     * Save the signed user information
     * See more {@linkplain com.techlooper.repository.couchbase.UserRepository}
     *
     * @param userInfo user information
     */
    void save(UserInfo userInfo);

    /**
     * Find the registered user by id
     * See more {@linkplain com.techlooper.repository.couchbase.UserRepository}
     *
     * @param id unique id
     * @return {@linkplain com.techlooper.entity.UserEntity}
     */
    UserEntity findById(String id);

    /**
     * Find user information by key
     * See more {@linkplain com.techlooper.repository.couchbase.UserRepository}
     *
     * @param key the key
     * @return {@linkplain com.techlooper.model.UserInfo}
     */
    UserInfo findUserInfoByKey(String key);

    /**
     * See more {@linkplain com.techlooper.repository.couchbase.UserRepository}
     *
     * @param key the key
     * @return {@linkplain com.techlooper.entity.UserEntity}
     */
    UserEntity findUserEntityByKey(String key);

    /**
     * Checks does user exist on VietnamWorks or not
     *
     * @param userEntity the user to check
     * @return true if user exists then the profile will be loaded
     */
    boolean verifyVietnamworksAccount(UserEntity userEntity);

    /**
     * Register more information of the user
     *
     * @param userInfo user information
     * @return returns true if the registration is successful.
     */
    boolean registerVietnamworksAccount(UserInfo userInfo);

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

    void registerUser(UserInfo userInfo);

    long countRegisteredUser();
}
