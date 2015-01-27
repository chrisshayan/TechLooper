package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.UserProfile;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;

import static com.techlooper.model.SocialProvider.FACEBOOK;

/**
 * Created by phuonghqh on 12/15/14.
 */
@Service("FACEBOOKService")
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
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

  public UserProfile getProfile(AccessGrant accessGrant) {
    Connection<Facebook> connection = facebookConnectionFactory.createConnection(getAccessGrant(accessGrant));
    FacebookProfile profile = connection.getApi().userOperations().getUserProfile();
    com.techlooper.entity.FacebookProfile fbProfile = dozerBeanMapper.map(profile, com.techlooper.entity.FacebookProfile.class);
    String profileImageUrl = connection.getApi().restOperations().getForObject(socialConfig.getApiUrl().get("picture"), JsonNode.class).at("/data/url").asText();
    fbProfile.setProfileImageUrl(profileImageUrl);
    fbProfile.setAccessGrant(accessGrant);
    return fbProfile;
  }
}
