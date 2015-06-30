package com.techlooper.config.web.sec;

import com.techlooper.entity.vnw.RoleName;
import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.repository.vnw.VnwUserRepo;
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
import java.util.Base64;

/**
 * Created by phuonghqh on 6/25/15.
 */
public class InternalAuthenticationManager implements AuthenticationManager {

  private static final Logger LOGGER = LoggerFactory.getLogger(InternalAuthenticationManager.class);

  @Resource
  private VnwUserRepo vnwUserRepo;

  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, "Unsupported authentication type");
    Assert.isTrue(!authentication.isAuthenticated(), "Already authenticated");
    if (!StringUtils.hasText(authentication.getPrincipal().toString())) {
      throw new InternalAuthenticationServiceException("User key must not be empty.");
    }

    Base64.Decoder decoder = Base64.getDecoder();
    String username = new String(decoder.decode(authentication.getPrincipal().toString()));
    String password = new String(decoder.decode(authentication.getCredentials().toString()));
    String hashPassword = org.apache.commons.codec.digest.DigestUtils.md5Hex(password);
    VnwUser vnwUser = vnwUserRepo.findByUsernameIgnoreCaseAndUserPassAndRoleName(username, hashPassword, RoleName.EMPLOYER);
    if (vnwUser != null) {
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(vnwUser.getUsername(), null,
        Arrays.asList(new SimpleGrantedAuthority(RoleName.EMPLOYER.name())));
      return auth;
    }

    LOGGER.debug("User [{}] does not exist in DB", authentication.getPrincipal().toString());
    throw new InternalAuthenticationServiceException("User does not exist in database.");
  }
}
