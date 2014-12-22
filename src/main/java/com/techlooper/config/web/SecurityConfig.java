package com.techlooper.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Created by NguyenDangKhoa on 11/27/14.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//  private static final String AUTHORIZATION_ROLE_ADMIN = "administrator";

//  @Value("${admin.path}")
//  private String adminPath;
//
//  @Value("${admin.username}")
//  private String username;
//
//  @Value("${admin.password}")
//  private String password;

  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.inMemoryAuthentication().withUser(username).password(password).roles(AUTHORIZATION_ROLE_ADMIN);
    auth.authenticationProvider(authenticationProvider());
  }

  protected void configure(HttpSecurity http) throws Exception {
//    http.authorizeRequests().anyRequest().permitAll();
//    http
//      .authorizeRequests()
//        .antMatchers(adminPath).hasRole(AUTHORIZATION_ROLE_ADMIN)
//      .and().httpBasic();
    http.csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and().authorizeRequests().antMatchers("/modules/register/**").hasRole("USER")
      .and().authorizeRequests().anyRequest().permitAll();
  }

//  public void configure(WebSecurity web) throws Exception {
//    web.ignoring().antMatchers("/images/**", "/css/**", "/generate-resources/**", "/modules/**", "/auth/**", "/getSocialConfig");
//  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    return new SecurityAuthenticationProvider();
  }
}