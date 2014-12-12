package com.techlooper.service;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;

/**
 * Created by phuonghqh on 12/11/14.
 */
public interface LinkedInService {

  String getAccessToken(String accessCode);
}
