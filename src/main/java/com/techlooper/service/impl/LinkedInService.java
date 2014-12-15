package com.techlooper.service.impl;

import com.techlooper.entity.LinkedInProfile;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.SocialService;
import com.techlooper.service.UserService;
import org.dozer.Mapper;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Optional;

import static com.techlooper.model.SocialProvider.LINKEDIN;

/**
 * Created by phuonghqh on 12/11/14.
 */

@Service("LINKEDINService")
public class LinkedInService extends AbstractSocialService {

  @Resource
  private LinkedInConnectionFactory linkedInConnectionFactory;

  @Inject
  public LinkedInService(JsonConfigRepository jsonConfigRepository) {
    super(jsonConfigRepository, LINKEDIN);
  }

  public OAuth2ConnectionFactory getOAuth2ConnectionFactory() {
    return linkedInConnectionFactory;
  }

  public UserEntity persistProfile(AccessGrant accessGrant) {
    Connection<LinkedIn> connection = linkedInConnectionFactory.createConnection(accessGrant);
    LinkedInProfileFull profile = connection.getApi().profileOperations().getUserProfileFull();
    LinkedInProfile profileEntity = dozerBeanMapper.map(profile, LinkedInProfile.class);
    UserEntity userEntity = Optional.ofNullable(userService.findById(profile.getEmailAddress())).orElse(new UserEntity());
    UserEntity.Builder builder = UserEntity.Builder.get(userEntity)
      .withProfile(SocialProvider.LINKEDIN, profileEntity)
      .withAccessGrant(dozerBeanMapper.map(accessGrant, com.techlooper.entity.AccessGrant.class));
    if (!Optional.ofNullable(userEntity.getEmailAddress()).isPresent()) {
      builder.withId(profile.getEmailAddress())
        .withLoginSource(SocialProvider.LINKEDIN)
        .withFirstName(profile.getFirstName())
        .withLastName(profile.getLastName())
        .withEmailAddress(profile.getEmailAddress())
        .withKey(passwordEncryptor.encryptPassword(profile.getEmailAddress()));
    }
    userService.save(userEntity);
    return userEntity;
  }
}
