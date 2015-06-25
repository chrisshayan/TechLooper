package com.techlooper.config.web.sec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by phuonghqh on 6/25/15.
 */
public class InternalAuthenticationManager implements AuthenticationManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(InternalAuthenticationManager.class);

//  @Resource
//  private UserService userService;

//  private static List<RoleNameEnum> ACCEPT_ROLES = Arrays.asList(RoleNameEnum.EXTERNAL_ADMIN, RoleNameEnum.BIZI_EMPLOYEE, RoleNameEnum.SUPER_USER);

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, "Unsupported authentication type");
    Assert.isTrue(!authentication.isAuthenticated(), "Already authenticated");
    if (!StringUtils.hasText(authentication.getPrincipal().toString())) {
      throw new InternalAuthenticationServiceException("User key must not be empty.");
    }

//    UserInfo userInfo = userService.hasUser(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
//    if (userInfo != null && ACCEPT_ROLES.contains(userInfo.getUserRole().getRoleName())) {
//      Set<SimpleGrantedAuthority> authorities = userInfo.getUserRole().getUserPermissions().stream()
//        .filter(userPermission -> userPermission.getStatus() == EntityStatus.ALLOW)
//        .map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermission().name()))
//        .collect(Collectors.toSet());
//      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userInfo.getEmail(), null, authorities);
//      return auth;
//    }

    LOGGER.debug("User [{}] does not exist in DB", authentication.getPrincipal().toString());
    throw new InternalAuthenticationServiceException("User does not exist in database.");
  }
}
