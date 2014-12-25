package com.techlooper.config.web;

import com.techlooper.model.UserInfo;
import com.techlooper.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by NguyenDangKhoa on 12/25/14.
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserService userService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, "unsupported authentication type");
        Assert.isTrue(!authentication.isAuthenticated(), "already authenticated");
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // use the credentials to try to authenticate against the database
        if (authenticatedAgainstDatabase(token)) {
            return new UsernamePasswordAuthenticationToken(token.getPrincipal(), token.getPrincipal(), Arrays.asList(new SimpleGrantedAuthority("USER")));
        } else {
            throw new UsernameNotFoundException("Unable to authenticate against database");
        }
    }

    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean authenticatedAgainstDatabase(Authentication authentication) {
        String key = authentication.getPrincipal().toString();
        UserInfo userInfo = userService.findUserInfoByKey(key);
        return StringUtils.isNotEmpty(userInfo.getEmailAddress());
    }
}
