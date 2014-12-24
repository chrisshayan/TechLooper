package com.techlooper.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationProvider;
import org.springframework.social.security.SocialAuthenticationServiceRegistry;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

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

//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////    auth.inMemoryAuthentication().withUser(username).password(password).roles(AUTHORIZATION_ROLE_ADMIN);
//    auth.authenticationProvider(socialAuthenticationProvider());
//  }
//
//  @Bean
//  public AuthenticationProvider socialAuthenticationProvider() {
//    return new SocialAuthenticationProvider(inMemoryUsersConnectionRepository(), socialUserDetailsService());
//  }
//
//  @Bean
//  public SocialUserDetailsService socialUserDetailsService() {
//    return new SimpleSocialUserDetailsService(userDetailsService());
//  }
//
//  @Bean
//  public UsersConnectionRepository inMemoryUsersConnectionRepository() {
//    return new InMemoryUsersConnectionRepository(socialConnectionFactoryLocator());
//  }
//
//  @Bean
//  public ConnectionFactoryLocator socialConnectionFactoryLocator() {
//    return new SocialAuthenticationServiceRegistry();
//  }


  protected void configure(HttpSecurity http) throws Exception {
//    http.authorizeRequests().anyRequest().permitAll();
//    http
//      .authorizeRequests()
//        .antMatchers(adminPath).hasRole(AUTHORIZATION_ROLE_ADMIN)
//      .and().httpBasic();
    http.csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
      .and().authorizeRequests().antMatchers("/user").hasAuthority("USER")
//      .and().formLogin().loginPage("/#/signin")
//      .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
      .and().authorizeRequests().anyRequest().permitAll();
  }

//  public void configure(WebSecurity web) throws Exception {
//    web.ignoring().antMatchers("/images/**", "/css/**", "/generate-resources/**", "/modules/**", "/auth/**", "/getSocialConfig");
//  }
}