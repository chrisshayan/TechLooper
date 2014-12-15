package com.techlooper.config;

import com.techlooper.model.SocialConfig;
import com.techlooper.repository.JsonConfigRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;

import javax.annotation.Resource;

import static com.techlooper.model.SocialProvider.FACEBOOK;
import static com.techlooper.model.SocialProvider.LINKEDIN;

/**
 * Created by phuonghqh on 12/9/14.
 */
@Configuration
public class SocialConfiguration {

  @Resource
  private JsonConfigRepository jsonConfigRepository;

  @Bean
  public ConnectionFactory linkedInConnectionFactory() {
    SocialConfig socialConfig = jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> LINKEDIN == config.getProvider()).findFirst().get();
    return new LinkedInConnectionFactory(socialConfig.getApiKey(), socialConfig.getSecretKey());
  }

  @Bean
  public ConnectionFactory facebookConnectionFactory() {
    SocialConfig socialConfig = jsonConfigRepository.getSocialConfig().stream()
      .filter(config -> FACEBOOK == config.getProvider()).findFirst().get();
    return new FacebookConnectionFactory(socialConfig.getApiKey(), socialConfig.getSecretKey());
  }
}
