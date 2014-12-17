package com.techlooper.service;

import com.techlooper.entity.AccessGrant;
import com.techlooper.entity.LinkedInProfile;
import com.techlooper.entity.UserEntity;

/**
 * Created by phuonghqh on 12/12/14.
 */
public interface SocialService {
  AccessGrant getAccessGrant(String accessCode);
  AccessGrant getAccessGrant(String accessToken, String accessSecret);
  UserEntity persistProfile(AccessGrant accessGrant);
}
