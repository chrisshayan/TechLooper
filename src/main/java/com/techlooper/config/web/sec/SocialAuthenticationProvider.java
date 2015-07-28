package com.techlooper.config.web.sec;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

/**
 * Created by phuonghqh on 7/28/15.
 */
public class SocialAuthenticationProvider  implements AuthenticationProvider {

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//    Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, "Unsupported authentication type");
    System.out.println(authentication);
    return null;
  }

  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
