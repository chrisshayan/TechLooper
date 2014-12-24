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
import java.util.*;

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

  public UserEntity saveFootprint(AccessGrant accessGrant) {
    Connection<GitHub> connection = gitHubConnectionFactory.createConnection(getAccessGrant(accessGrant));
    UserEntity userEntity = getForUserProfile(accessGrant, connection);
    com.techlooper.entity.GitHubUserProfile profile = (com.techlooper.entity.GitHubUserProfile) userEntity.getProfiles().get(GITHUB);
    getForUserRepos(connection, profile);
    getForUserFollowers(connection, profile);
    userService.save(userEntity);
    return userEntity;
  }

  private void getForUserFollowers(Connection<GitHub> connection, com.techlooper.entity.GitHubUserProfile profile) {
    String username = profile.getUsername();
    GitHubFollower[] followers = connection.getApi().restOperations()
      .getForObject(socialConfig.getApiUrl().get("followers"), GitHubFollower[].class, username);
    profile.setFollowers(Arrays.asList(followers));
  }

  private void getForUserRepos(Connection<GitHub> connection, com.techlooper.entity.GitHubUserProfile profile) {
    String username = profile.getUsername();
    GitHubRepo[] repos = connection.getApi().restOperations()
      .getForObject(socialConfig.getApiUrl().get("repos"), GitHubRepo[].class, username);
    profile.setRepos(Arrays.asList(repos));
  }

  private UserEntity getForUserProfile(AccessGrant accessGrant, Connection<GitHub> connection) {
    GitHubUserProfile profile = connection.getApi().userOperations().getUserProfile();
    com.techlooper.entity.GitHubUserProfile profileEntity = dozerBeanMapper.map(profile, com.techlooper.entity.GitHubUserProfile.class);
    UserEntity userEntity = Optional.ofNullable(userService.findById(profile.getEmail())).orElse(new UserEntity());
    UserEntity.UserEntityBuilder builder = userEntity(userEntity)
      .withProfile(GITHUB, profileEntity)
      .withAccessGrant(dozerBeanMapper.map(accessGrant, AccessGrant.class));
    if (!Optional.ofNullable(userEntity.getEmailAddress()).isPresent()) {
      builder.withId(profile.getEmail())
        .withLoginSource(GITHUB)
        .withFirstName(profile.getName())
        .withEmailAddress(profile.getEmail())
        .withKey(passwordEncryptor.encryptPassword(profile.getEmail()));
    }
    return userEntity;
  }
}
