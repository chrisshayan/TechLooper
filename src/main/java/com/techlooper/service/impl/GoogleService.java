package com.techlooper.service.impl;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.SimpleUserProfile;
import com.techlooper.entity.UserProfile;
import com.techlooper.model.SocialConfig;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;

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

  public org.springframework.social.connect.UserProfile getUserProfile(String code, SocialConfig socialConfig) {
    org.springframework.social.oauth2.AccessGrant access = googleConnectionFactory.getOAuthOperations().exchangeForAccess(code, socialConfig.getRedirectUri(), null);
    org.springframework.social.connect.UserProfile userProfile = googleConnectionFactory.createConnection(access).fetchUserProfile();
    return userProfile;
  }
}
