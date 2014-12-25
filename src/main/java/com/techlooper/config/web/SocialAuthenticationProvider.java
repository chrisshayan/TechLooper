package com.techlooper.config.web;

import com.techlooper.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;

/**
 * Created by NguyenDangKhoa on 12/25/14.
 */
public class SocialAuthenticationProvider implements AuthenticationProvider {

  @Resource
  private UserService userService;

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, "Unsupported authentication type");
    Assert.isTrue(!authentication.isAuthenticated(), "Already authenticated");
    Assert.isTrue(Optional.ofNullable(userService.findUserEntityByKey(authentication.getPrincipal().toString())).isPresent(), "Invalid User");
    UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
    return new UsernamePasswordAuthenticationToken(token.getPrincipal(), token.getPrincipal(), Arrays.asList(new SimpleGrantedAuthority("USER")));
  }

  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
