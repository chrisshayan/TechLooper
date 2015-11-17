package com.techlooper.config.web.sec;

import com.techlooper.model.SocialProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by phuonghqh on 6/25/15.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Resource
  private Environment environment;

  @Bean
  public AuthenticationProvider vnwAuthenticationProvider() {
    return new VnwAuthenticationProvider();
  }

  @Bean
  public AuthenticationProvider socialAuthenticationProvider() {
    return new SocialAuthenticationProvider();
  }

  @Bean
  public AuthenticationProvider switchingAuthenticationProvider() {
    SwitchingAuthenticationProvider switchingAuthenticationProvider = new SwitchingAuthenticationProvider();
    Map<SocialProvider, AuthenticationProvider> authenticationProviders = new HashMap<>();
    authenticationProviders.put(SocialProvider.VIETNAMWORKS, vnwAuthenticationProvider());
    authenticationProviders.put(SocialProvider.FACEBOOK, socialAuthenticationProvider());
    authenticationProviders.put(SocialProvider.GOOGLE, socialAuthenticationProvider());
    switchingAuthenticationProvider.setProviders(authenticationProviders);
    return switchingAuthenticationProvider;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(Arrays.asList(switchingAuthenticationProvider()));
  }

  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.headers().frameOptions().disable();
    http.authorizeRequests()
      .and().formLogin().loginPage("/login").usernameParameter("us").passwordParameter("pwd").successHandler(getSuccessHandler()).failureHandler(getAuthenticationFailureHandler())
      .and().rememberMe().rememberMeParameter("rm").key(environment.getProperty("core.textEncryptor.password")).tokenValiditySeconds(SessionListener.MAX_INACTIVE_INTERVAL)
      .and().logout().logoutUrl("/logout").logoutSuccessHandler(getLogoutSuccessHandler()).invalidateHttpSession(true).deleteCookies("JSESSIONID").permitAll()
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