package com.techlooper.service.impl;

import com.techlooper.service.LinkedInService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.connect.*;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 12/11/14.
 */

@Service
public class LinkedInServiceImpl implements LinkedInService {

  @Resource
  private ConnectionFactory linkedInConnectionFactory;

  @Value("${social.redirectUrl}")
  private String socialRedirectUrl;

  public String getAccessToken(String accessCode) {
    LinkedInConnectionFactory factory = (LinkedInConnectionFactory)linkedInConnectionFactory;
    AccessGrant accessGrant = factory.getOAuthOperations().exchangeForAccess(accessCode, "http://localhost:8080/techlooper/authentication", null);
//    Connection<LinkedIn> connection = fac.createConnection(accessGrant);
//    connection.getKey();
//    AccessGrant accessGrant = factory.getOAuthOperations().exchangeForAccess(accessCode, socialRedirectUrl, null);
//    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(connectionKey.getProviderUserId(), null, null));
    return accessGrant.getAccessToken();
  }
}
