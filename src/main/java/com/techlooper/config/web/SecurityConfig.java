package com.techlooper.config.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by NguyenDangKhoa on 11/27/14.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new SocialAuthenticationProvider();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .authorizeRequests().antMatchers("/user").hasAuthority("USER")
                .and().formLogin().loginPage("/login").usernameParameter("key").defaultSuccessUrl("/")
                .failureHandler((request, response, exception) -> {
                    LOGGER.error(exception.getMessage(), exception);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                })
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID").invalidateHttpSession(true)
                .and().exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    LOGGER.error(authException.getMessage(), authException);
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                })
                .and().authorizeRequests().anyRequest().permitAll()
                .and().sessionManagement().invalidSessionUrl("/").maximumSessions(1);
    }

  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/images/**", "/css/**", "/generate-resources/**", "/modules/**", "/bower_components/**", "/custom-js/**");
  }
}