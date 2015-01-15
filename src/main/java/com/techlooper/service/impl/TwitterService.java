package com.techlooper.service.impl;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.UserEntity;
import com.techlooper.entity.UserProfile;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.util.Optional;

import static com.techlooper.entity.UserEntity.UserEntityBuilder.userEntity;
import static com.techlooper.model.SocialProvider.TWITTER;

/**
 * Created by phuonghqh on 12/16/14.
 */
@Service("TWITTERService")
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
public class TwitterService extends AbstractSocialService {

  @Resource
  private TwitterConnectionFactory twitterConnectionFactory;

  @Inject
  public TwitterService(JsonConfigRepository jsonConfigRepository) {
    super(jsonConfigRepository, TWITTER);
  }

  public OAuth1ConnectionFactory getOAuth1ConnectionFactory() {
    return twitterConnectionFactory;
  }

  public UserProfile getProfile(AccessGrant accessGrant) {
    Connection<Twitter> connection = twitterConnectionFactory.createConnection(new OAuthToken(accessGrant.getValue(), accessGrant.getSecret()));
    TwitterProfile profile = connection.getApi().userOperations().getUserProfile();
    com.techlooper.entity.TwitterProfile twProfile = dozerBeanMapper.map(profile, com.techlooper.entity.TwitterProfile.class);
    twProfile.setAccessGrant(accessGrant);
    return twProfile;
  }
}
