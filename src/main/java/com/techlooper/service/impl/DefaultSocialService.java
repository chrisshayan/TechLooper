package com.techlooper.service.impl;

import com.techlooper.entity.UserEntity;
import com.techlooper.service.SocialService;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Service;

/**
 * Created by phuonghqh on 12/12/14.
 */

@Service
public class DefaultSocialService implements SocialService {

  public AccessGrant getAccessGrant(String accessCode) {
    return new AccessGrant("");
  }

  public UserEntity persistProfile(AccessGrant accessGrant) {
    return new UserEntity();
  }
}
