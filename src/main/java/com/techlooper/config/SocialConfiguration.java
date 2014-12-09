package com.techlooper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;

/**
 * Created by phuonghqh on 12/9/14.
 */
//@Configuration
//@EnableSocial
public class SocialConfiguration implements SocialConfigurer {

  @Bean
  public LinkedIn linkedin(ConnectionRepository repository) {
    Connection<LinkedIn> connection = repository.findPrimaryConnection(LinkedIn.class);
    return connection != null ? connection.getApi() : null;
  }

  public void addConnectionFactories(ConnectionFactoryConfigurer configurer, Environment env) {
    configurer.addConnectionFactory(
      new LinkedInConnectionFactory(env.getProperty("linkedin.appKey"), env.getProperty("linkedin.appSecret")));
  }

  public UserIdSource getUserIdSource() {
    return new UserIdSource() {
      public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
          throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
        }
        return authentication.getName();
      }
    };
  }

  public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
    //return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    return null;
  }
}
