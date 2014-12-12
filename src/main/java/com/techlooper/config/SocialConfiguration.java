package com.techlooper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 12/9/14.
 */
@Configuration
public class SocialConfiguration {

  @Resource
  private Environment env;

  @Bean
  public ConnectionFactory linkedInConnectionFactory() {
    return new LinkedInConnectionFactory(env.getProperty("linkedin.appKey"), env.getProperty("linkedin.appSecret"));
  }
}
