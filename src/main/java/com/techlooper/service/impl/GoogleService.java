package com.techlooper.service.impl;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.SimpleUserProfile;
import com.techlooper.entity.UserEntity;
import com.techlooper.entity.UserProfile;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.plus.Person;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Optional;

import static com.techlooper.entity.UserEntity.UserEntityBuilder.userEntity;

/**
 * Created by phuonghqh on 12/16/14.
 */
@Service("GOOGLEService")
public class GoogleService extends AbstractSocialService {

  @Resource
  private GoogleConnectionFactory googleConnectionFactory;

  @Inject
  public GoogleService(JsonConfigRepository jsonConfigRepository) {
    super(jsonConfigRepository, SocialProvider.GOOGLE);
  }

  public OAuth2ConnectionFactory getOAuth2ConnectionFactory() {
    return googleConnectionFactory;
  }

  public UserProfile getProfile(AccessGrant accessGrant) {
    Connection<Google> connection = googleConnectionFactory.createConnection(getAccessGrant(accessGrant));
    SimpleUserProfile gProfile = new SimpleUserProfile();
    gProfile.setAccessGrant(accessGrant);
    gProfile.setActual(connection.getApi().plusOperations().getGoogleProfile());
    return gProfile;
  }

  public UserEntity saveFootprint(AccessGrant accessGrant) {
    Person profile = (Person) ((SimpleUserProfile) getProfile(accessGrant)).getActual();
    UserEntity userEntity = Optional.ofNullable(userService.findById(profile.getAccountEmail())).orElse(new UserEntity());
    UserEntity.UserEntityBuilder builder = userEntity(userEntity)
      .withProfile(SocialProvider.GOOGLE, profile)
      .withAccessGrant(dozerBeanMapper.map(accessGrant, AccessGrant.class));
    if (!Optional.ofNullable(userEntity.getEmailAddress()).isPresent()) {
      dozerBeanMapper.map(profile, userEntity);
      builder.withId(profile.getAccountEmail())
        .withLoginSource(SocialProvider.GITHUB);
    }
    userService.save(userEntity);
    return userEntity;
  }
}
