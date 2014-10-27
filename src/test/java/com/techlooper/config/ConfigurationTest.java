package com.techlooper.config;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;


/**
 * Created by phuonghqh on 10/20/14.
 */

@Configuration
@PropertySources({
  @PropertySource("classpath:techlooper.properties"),
  @PropertySource("classpath:secret.properties")})
public class ConfigurationTest implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public String vnwConfigurationJson() throws IOException {
    return IOUtils.toString(applicationContext.getResource("classpath:expect/vnw-configuration.json").getInputStream(), "UTF-8");
  }

  @Bean
  public String vnwJobSearchJson() throws IOException {
    return IOUtils.toString(applicationContext.getResource("classpath:expect/vnw-jobs.json").getInputStream(), "UTF-8");
  }

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
