package com.techlooper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.linkedin.api.LinkedIn;

/**
 * Created by phuonghqh on 12/9/14.
 */
@Configuration
@EnableSocial
public class SocialConfiguration {

  @Bean
  public LinkedIn linkedin(ConnectionRepository repository) {
    Connection<LinkedIn> connection = repository.findPrimaryConnection(LinkedIn.class);
    return connection != null ? connection.getApi() : null;
  }
}
