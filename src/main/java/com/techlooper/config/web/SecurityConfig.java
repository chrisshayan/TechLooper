package com.techlooper.config.web;

import com.techlooper.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationProvider;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

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

  @Resource
  private UserService userService;

  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.inMemoryAuthentication().withUser(username).password(password).roles(AUTHORIZATION_ROLE_ADMIN);
    auth.authenticationProvider(new AuthenticationProvider() {

      public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication, "unsupported authentication type");
        Assert.isTrue(!authentication.isAuthenticated(), "already authenticated");
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        return new UsernamePasswordAuthenticationToken(token.getPrincipal(), token.getPrincipal(), Arrays.asList(new SimpleGrantedAuthority("USER")));
      }

      public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
      }
    });
//    auth.userDetailsService(userService);
  }

  protected void configure(HttpSecurity http) throws Exception {
//    http.authorizeRequests().anyRequest().permitAll();
//    http
//      .authorizeRequests()
//        .antMatchers(adminPath).hasRole(AUTHORIZATION_ROLE_ADMIN)
//      .and().httpBasic();
    http.csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
      .and().authorizeRequests().antMatchers("/user").hasAuthority("USER")
      .and().formLogin().loginPage("/login").usernameParameter("key").defaultSuccessUrl("/")
      .and().logout().logoutUrl("/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID").invalidateHttpSession(true)
      .and().authorizeRequests().anyRequest().permitAll();
  }

//  public void configure(WebSecurity web) throws Exception {
//    web.ignoring().antMatchers("/images/**", "/css/**", "/generate-resources/**", "/modules/**", "/auth/**", "/getSocialConfig");
//  }
}