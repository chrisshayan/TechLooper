package com.techlooper.service;

import org.springframework.social.oauth2.AccessGrant;

/**
 * Created by phuonghqh on 12/12/14.
 */
public interface SocialService {

  AccessGrant getAccessGrant(String accessCode);

  void persistProfile(AccessGrant accessGrant);
}
