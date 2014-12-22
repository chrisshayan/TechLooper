package com.techlooper.config.web;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by phuonghqh on 12/22/14.
 */
public class SecurityAuthenticationProvider implements AuthenticationProvider {

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//    String name = authentication.getName();
//    String password = authentication.getCredentials().toString();
//
//    // use the credentials to try to authenticate against the third party system
//    if (authenticatedAgainstThirdPartySystem()) {
//      List<GrantedAuthority> grantedAuths = new ArrayList<>();
//      return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
//    } else {
//      throw new AuthenticationException("Unable to auth against third party systems");
//    }
    return null;
  }

  public boolean supports(Class<?> clazz) {
    return clazz.equals(UsernamePasswordAuthenticationToken.class);
  }
}
