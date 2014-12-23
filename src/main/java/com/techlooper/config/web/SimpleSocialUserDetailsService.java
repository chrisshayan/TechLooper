package com.techlooper.config.web;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * Created by phuonghqh on 12/23/14.
 */
public class SimpleSocialUserDetailsService implements SocialUserDetailsService {

  private UserDetailsService userDetailsService;

  public SimpleSocialUserDetailsService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
    UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
    return new SocialUser(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
  }
}
