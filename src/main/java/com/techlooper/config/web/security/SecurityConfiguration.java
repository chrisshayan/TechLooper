package com.techlooper.config.web.security;

import com.techlooper.repository.EmbeddedRedisServer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

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
      .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)).and()
      .authorizeRequests().antMatchers("/user").hasAuthority("USER")
      .and().formLogin().loginPage("/login").usernameParameter("key").failureHandler((request, response, exception) -> {response.setStatus(HttpServletResponse.SC_FORBIDDEN);})
      .and().logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
      .and().exceptionHandling().authenticationEntryPoint((request, response, authException) -> {response.setStatus(HttpServletResponse.SC_FORBIDDEN);})
      .and().authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated();
//      .and().sessionManagement().invalidSessionUrl("/").maximumSessions(1);
  }

  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/images/**", "/css/**", "/generate-resources/**", "/modules/**", "/bower_components/**", "/custom-js/**");
  }

}