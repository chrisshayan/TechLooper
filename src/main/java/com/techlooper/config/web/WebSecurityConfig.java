package com.techlooper.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by NguyenDangKhoa on 11/27/14.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${admin.path}")
    private String adminPath;

    @Value("${admin.username}")
    private String username;

    @Value("${admin.password}")
    private String password;

    private static final String AUTHORIZATION_ROLE_ADMIN = "administrator";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(username).password(password).roles(AUTHORIZATION_ROLE_ADMIN);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(adminPath).hasRole(AUTHORIZATION_ROLE_ADMIN).and().httpBasic();
        http.csrf().disable();
    }
}