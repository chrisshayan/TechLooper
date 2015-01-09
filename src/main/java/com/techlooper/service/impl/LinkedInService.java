package com.techlooper.service.impl;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.LinkedInProfile;
import com.techlooper.entity.UserEntity;
import com.techlooper.entity.UserEntity.UserEntityBuilder;
import com.techlooper.entity.UserProfile;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Optional;

import static com.techlooper.entity.UserEntity.UserEntityBuilder.userEntity;
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

  public UserProfile getProfile(AccessGrant accessGrant) {
    Connection<LinkedIn> connection = linkedInConnectionFactory.createConnection(getAccessGrant(accessGrant));
    LinkedInProfileFull profile = connection.getApi().profileOperations().getUserProfileFull();
    LinkedInProfile liProfile = dozerBeanMapper.map(profile, LinkedInProfile.class);
    liProfile.setAccessGrant(accessGrant);
    return liProfile;
  }

  public UserEntity saveFootprint(AccessGrant accessGrant) {
    LinkedInProfile profileEntity = (LinkedInProfile) getProfile(accessGrant);
    UserEntity entity = Optional.ofNullable(userService.findById(profileEntity.getEmailAddress())).orElse(new UserEntity());
    UserEntityBuilder builder = userEntity(entity)
      .withProfile(socialConfig.getProvider(), profileEntity)
      .withAccessGrant(dozerBeanMapper.map(accessGrant, AccessGrant.class));
    if (!Optional.ofNullable(entity.getEmailAddress()).isPresent()) {
      dozerBeanMapper.map(profileEntity, entity);
      builder.withId(profileEntity.getEmailAddress())
        .withLoginSource(socialConfig.getProvider());
    }
    userService.save(entity);
    return entity;
  }
}
