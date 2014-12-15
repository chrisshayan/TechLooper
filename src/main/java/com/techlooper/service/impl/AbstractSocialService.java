package com.techlooper.service.impl;

import com.techlooper.model.SocialConfig;
import com.techlooper.model.SocialProvider;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.SocialService;
import com.techlooper.service.UserService;
import org.dozer.Mapper;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

import javax.annotation.Resource;

import static com.techlooper.model.SocialProvider.FACEBOOK;

/**
 * Created by phuonghqh on 12/15/14.
 */
public abstract class AbstractSocialService implements SocialService {

  private SocialConfig socialConfig;

  @Resource
  protected UserService userService;

  @Resource
  protected Mapper dozerBeanMapper;

  @Resource
  protected PasswordEncryptor passwordEncryptor;

  public AbstractSocialService(JsonConfigRepository jsonConfigRepository, SocialProvider socialProvider) {
    socialConfig = jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> socialProvider == config.getProvider()).findFirst().get();
  }

  public AccessGrant getAccessGrant(String accessCode) {
    return getOAuth2ConnectionFactory().getOAuthOperations().exchangeForAccess(accessCode, socialConfig.getRedirectUri(), null);
  }

  public abstract OAuth2ConnectionFactory getOAuth2ConnectionFactory();
}
