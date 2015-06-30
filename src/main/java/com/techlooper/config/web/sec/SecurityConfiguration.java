package com.techlooper.config.web.sec;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by phuonghqh on 6/25/15.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Bean
  public AuthenticationManager authenticationManager() {
    return new InternalAuthenticationManager();
  }

  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
      .authorizeRequests()
      .and().formLogin().loginPage("/login").usernameParameter("us").passwordParameter("pwd").successHandler(getSuccessHandler()).failureHandler(getAuthenticationFailureHandler())
      .and().logout().logoutUrl("/logout").logoutSuccessHandler(getLogoutSuccessHandler()).invalidateHttpSession(true).deleteCookies("SESSION").permitAll()
      .and().exceptionHandling().authenticationEntryPoint(exceptionHandler());
  }

  private AuthenticationEntryPoint exceptionHandler() {
    return (request, response, authException) -> response.setStatus(HttpServletResponse.SC_FORBIDDEN);
  }

  private LogoutSuccessHandler getLogoutSuccessHandler() {
    return (request, response, authentication) -> response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  private AuthenticationFailureHandler getAuthenticationFailureHandler() {
    return (request, response, exception) -> response.setStatus(HttpServletResponse.SC_FORBIDDEN);
  }

  private AuthenticationSuccessHandler getSuccessHandler() {
    return (request, response, authentication) -> response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/images/**", "/css/**", "/generate-resources/**", "/modules/**", "/bower_components/**", "/custom-js/**");
  }
}