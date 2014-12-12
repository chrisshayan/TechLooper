package com.techlooper.service.impl;

import com.techlooper.service.SocialService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 12/11/14.
 */

@Service("linkedinService")
public class LinkedInServiceImpl implements SocialService {

  @Resource
  private ConnectionFactory linkedInConnectionFactory;

  @Value("${social.redirectUrl}")
  private String socialRedirectUrl;

  public String getAccessToken(String accessCode) {
    LinkedInConnectionFactory factory = (LinkedInConnectionFactory) linkedInConnectionFactory;
    AccessGrant accessGrant = factory.getOAuthOperations().exchangeForAccess(accessCode, socialRedirectUrl, null);
//    Connection<LinkedIn> connection = fac.createConnection(accessGrant);
//    connection.getKey();
//    AccessGrant accessGrant = factory.getOAuthOperations().exchangeForAccess(accessCode, socialRedirectUrl, null);
//    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(connectionKey.getProviderUserId(), null, null));
    return accessGrant.getAccessToken();
  }
}
