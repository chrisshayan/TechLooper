package com.techlooper.config.web.security;

import com.techlooper.service.UserService;
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
import java.util.Optional;

/**
 * Created by NguyenDangKhoa on 12/25/14.
 */
public class SocialAuthenticationManager implements AuthenticationManager {

    @Resource
    private UserService userService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, "Unsupported authentication type");
        Assert.isTrue(!authentication.isAuthenticated(), "Already authenticated");
        String key = authentication.getPrincipal().toString();
        if (!StringUtils.hasText(key)) {
            throw new InternalAuthenticationServiceException("User key must not be empty.");
        }
        if (!Optional.ofNullable(userService.findUserEntityByKey(key)).isPresent()) {
            throw new InternalAuthenticationServiceException("User does not exist in database.");
        }
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(token.getPrincipal(), token.getPrincipal(), Arrays.asList(new SimpleGrantedAuthority("USER")));
        return auth;
    }
}
