package com.techlooper.service.impl;

import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.protocol.views.Query;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.SocialProvider;
import com.techlooper.model.UserInfo;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.data.couchbase.cache.CouchbaseCache;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Optional;

import static com.techlooper.model.SocialProvider.FACEBOOK;

/**
 * Created by phuonghqh on 12/15/14.
 */
@Service("FACEBOOKService")
public class FacebookService extends AbstractSocialService {


  @Resource
  private FacebookConnectionFactory facebookConnectionFactory;

  @Inject
  public FacebookService(JsonConfigRepository jsonConfigRepository) {
    super(jsonConfigRepository, FACEBOOK);
  }

  public OAuth2ConnectionFactory getOAuth2ConnectionFactory() {
    return facebookConnectionFactory;
  }

  public UserEntity persistProfile(AccessGrant accessGrant) {
    Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant);
    FacebookProfile profile = connection.getApi().userOperations().getUserProfile();
    com.techlooper.entity.FacebookProfile profileEntity = dozerBeanMapper.map(profile, com.techlooper.entity.FacebookProfile.class);
    UserEntity userEntity = Optional.ofNullable(userService.findById(profile.getEmail())).orElse(new UserEntity());
    UserEntity.Builder builder = UserEntity.Builder.get(userEntity)
      .withProfile(SocialProvider.FACEBOOK, profileEntity)
      .withAccessGrant(dozerBeanMapper.map(accessGrant, com.techlooper.entity.AccessGrant.class));
    if (!Optional.ofNullable(userEntity.getEmailAddress()).isPresent()) {
      builder.withId(profile.getEmail())
        .withLoginSource(SocialProvider.FACEBOOK)
        .withFirstName(profile.getFirstName())
        .withLastName(profile.getLastName())
        .withEmailAddress(profile.getEmail())
        .withKey(passwordEncryptor.encryptPassword(profile.getEmail()));
    }
    userService.save(userEntity);
    return userEntity;
  }
}
