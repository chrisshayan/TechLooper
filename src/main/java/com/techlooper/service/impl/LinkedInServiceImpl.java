package com.techlooper.service.impl;

import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.SocialService;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;

import static com.techlooper.model.SocialProvider.LINKEDIN;

/**
 * Created by phuonghqh on 12/11/14.
 */

@Service("LINKEDINService")
public class LinkedInServiceImpl implements SocialService {

  @Resource
  private ConnectionFactory linkedInConnectionFactory;

  private String redirectUri;

//  @Resource
//  private JsonConfigRepository jsonConfigRepository;

  @Inject
  public LinkedInServiceImpl(JsonConfigRepository jsonConfigRepository) {
    redirectUri = jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> LINKEDIN == config.getProvider()).findFirst().get().getRedirectUri();
  }

  public String getAccessToken(String accessCode) {
    LinkedInConnectionFactory factory = (LinkedInConnectionFactory) linkedInConnectionFactory;
    AccessGrant accessGrant = factory.getOAuthOperations().exchangeForAccess(accessCode, redirectUri, null);
//    Connection<LinkedIn> connection = fac.createConnection(accessGrant);
//    connection.getKey();
//    AccessGrant accessGrant = factory.getOAuthOperations().exchangeForAccess(accessCode, redirectUri, null);
//    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(connectionKey.getProviderUserId(), null, null));
    return accessGrant.getAccessToken();
  }
}
