package com.techlooper.service.impl;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.github.api.GitHub;
import org.springframework.social.github.api.GitHubUserProfile;
import org.springframework.social.github.connect.GitHubConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Optional;

import static com.techlooper.entity.UserEntity.UserEntityBuilder.userEntity;

/**
 * Created by phuonghqh on 12/16/14.
 */
@Service("GITHUBService")
public class GitHubService extends AbstractSocialService {

  @Resource
  private GitHubConnectionFactory gitHubConnectionFactory;

  @Inject
  public GitHubService(JsonConfigRepository jsonConfigRepository) {
    super(jsonConfigRepository, SocialProvider.GITHUB);
  }

  public OAuth2ConnectionFactory getOAuth2ConnectionFactory() {
    return gitHubConnectionFactory;
  }

  public UserEntity persistProfile(AccessGrant accessGrant) {
    Connection<GitHub> connection = gitHubConnectionFactory.createConnection(getAccessGrant(accessGrant));
    GitHubUserProfile profile = connection.getApi().userOperations().getUserProfile();
    com.techlooper.entity.GitHubUserProfile profileEntity = dozerBeanMapper.map(profile, com.techlooper.entity.GitHubUserProfile.class);
    UserEntity userEntity = Optional.ofNullable(userService.findById(profile.getEmail())).orElse(new UserEntity());
    UserEntity.UserEntityBuilder builder = userEntity(userEntity)
      .withProfile(SocialProvider.GITHUB, profileEntity)
      .withAccessGrant(dozerBeanMapper.map(accessGrant, AccessGrant.class));
    if (!Optional.ofNullable(userEntity.getEmailAddress()).isPresent()) {
      builder.withId(profile.getEmail())
        .withLoginSource(SocialProvider.GITHUB)
        .withFirstName(profile.getName())
        .withEmailAddress(profile.getEmail())
        .withKey(passwordEncryptor.encryptPassword(profile.getEmail()));
    }
    userService.save(userEntity);
    return userEntity;
  }
}
