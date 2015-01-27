package com.techlooper.config.web.security;

import com.techlooper.repository.EmbeddedRedisServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.SessionRepositoryFilter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by NguyenDangKhoa on 11/27/14.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableRedisHttpSession
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Bean
  public EmbeddedRedisServer redisServer() {
    return new EmbeddedRedisServer();
  }

  @Bean
  public JedisConnectionFactory connectionFactory() throws Exception {
    return new JedisConnectionFactory();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new SocialAuthenticationManager();
  }

  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
//      .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)).and()
      .authorizeRequests().antMatchers("/user/**").hasAuthority("USER")
      .and().formLogin().loginPage("/login").usernameParameter("key").successHandler(getSuccessHandler()).failureHandler(getAuthenticationFailureHandler())
      .and().logout().logoutUrl("/logout").logoutSuccessHandler(getLogoutSuccessHandler()).invalidateHttpSession(true).deleteCookies("SESSION").permitAll()
      .and().exceptionHandling().authenticationEntryPoint(getAuthenticationEntryPoint());
//      .and().sessionManagement().maximumSessions(1);
  }

  private AuthenticationEntryPoint getAuthenticationEntryPoint() {
    return (request, response, authException) -> {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    };
  }

  private LogoutSuccessHandler getLogoutSuccessHandler() {
    return (request, response, authentication) -> {
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    };
  }

  private AuthenticationFailureHandler getAuthenticationFailureHandler() {
    return (request, response, exception) -> {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    };
  }

  private AuthenticationSuccessHandler getSuccessHandler() {
    return (request, response, authentication) -> {
      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    };
  }

  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/images/**", "/css/**", "/generate-resources/**", "/modules/**", "/bower_components/**", "/custom-js/**");
  }

}