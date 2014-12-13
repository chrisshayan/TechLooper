package com.techlooper.service;

import com.techlooper.entity.LinkedInProfile;
import com.techlooper.entity.UserEntity;
import org.springframework.social.oauth2.AccessGrant;

/**
 * Created by phuonghqh on 12/12/14.
 */
public interface SocialService {

  AccessGrant getAccessGrant(String accessCode);

  UserEntity persistProfile(AccessGrant accessGrant);
}
