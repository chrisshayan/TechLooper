package com.techlooper.service.impl;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.GitHubFollower;
import com.techlooper.entity.GitHubRepo;
import com.techlooper.entity.UserEntity;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.techlooper.entity.UserEntity.UserEntityBuilder.userEntity;
import static com.techlooper.model.SocialProvider.GITHUB;

/**
 * Created by phuonghqh on 12/16/14.
 */
@Service("GITHUBService")
public class GitHubService extends AbstractSocialService {

  @Resource
  private GitHubConnectionFactory gitHubConnectionFactory;

  @Inject
  public GitHubService(JsonConfigRepository jsonConfigRepository) {
    super(jsonConfigRepository, GITHUB);
  }

  public OAuth2ConnectionFactory getOAuth2ConnectionFactory() {
    return gitHubConnectionFactory;
  }

  public Object getProfile(AccessGrant accessGrant) {
    Connection<GitHub> connection = gitHubConnectionFactory.createConnection(getAccessGrant(accessGrant));
    com.techlooper.entity.GitHubUserProfile profile = dozerBeanMapper.map(connection.getApi().userOperations().getUserProfile(), com.techlooper.entity.GitHubUserProfile.class);
    buildProfile(connection, profile);
    return profile;
  }

  public UserEntity saveFootprint(com.techlooper.entity.AccessGrant accessGrant, String key) {
    UserEntity entity = userService.findUserEntityByKey(key);
    userEntity(entity).withProfile(socialConfig.getProvider(), getProfile(accessGrant));
    userService.save(entity);
    return entity;
  }

  public UserEntity saveFootprint(AccessGrant accessGrant) {
    Connection<GitHub> connection = gitHubConnectionFactory.createConnection(getAccessGrant(accessGrant));
    UserEntity userEntity = createUserEntity(accessGrant, connection);
    com.techlooper.entity.GitHubUserProfile profile = (com.techlooper.entity.GitHubUserProfile) userEntity.getProfiles().get(GITHUB);
    buildProfile(connection, profile);
    userService.save(userEntity);
    return userEntity;
  }

  private void buildProfile(Connection<GitHub> connection, com.techlooper.entity.GitHubUserProfile profile) {
    String username = profile.getUsername();
    profile.setRepos(getForUserRepos(connection, username));
    profile.setFollowers(getForUserFollowers(connection, username));
  }

  private List<GitHubFollower> getForUserFollowers(Connection<GitHub> connection, String username) {
    return Arrays.asList(connection.getApi().restOperations()
      .getForObject(socialConfig.getApiUrl().get("followers"), GitHubFollower[].class, username));
  }

  private List<GitHubRepo> getForUserRepos(Connection<GitHub> connection, String username) {
    return Arrays.asList(connection.getApi().restOperations()
      .getForObject(socialConfig.getApiUrl().get("repos"), GitHubRepo[].class, username));
  }

  private UserEntity createUserEntity(AccessGrant accessGrant, Connection<GitHub> connection) {
    GitHubUserProfile profile = connection.getApi().userOperations().getUserProfile();
    com.techlooper.entity.GitHubUserProfile profileEntity = dozerBeanMapper.map(profile, com.techlooper.entity.GitHubUserProfile.class);
    UserEntity userEntity = Optional.ofNullable(userService.findById(profileEntity.getEmail())).orElse(new UserEntity());
    UserEntity.UserEntityBuilder builder = userEntity(userEntity)
      .withProfile(GITHUB, profileEntity)
      .withAccessGrant(dozerBeanMapper.map(accessGrant, AccessGrant.class));
    if (!Optional.ofNullable(userEntity.getEmailAddress()).isPresent()) {
      builder.withId(profileEntity.getEmail())
        .withLoginSource(GITHUB)
        .withFirstName(profileEntity.getName())
        .withEmailAddress(profileEntity.getEmail())
        .withKey(passwordEncryptor.encryptPassword(profileEntity.getEmail()));
    }
    return userEntity;
  }
}
