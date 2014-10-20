package com.techlooper.config;

import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;

import static freemarker.template.Configuration.VERSION_2_3_21;


/**
 * Created by phuonghqh on 10/20/14.
 */

@Configuration
@PropertySources({
  @PropertySource("classpath:techlooper.properties"),
  @PropertySource("classpath:secret.properties")})
@ComponentScan({"com.techlooper.consumer"})
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

  @Bean
  public freemarker.template.Configuration freemarkerConfiguration() throws IOException {
    freemarker.template.Configuration configuration = new freemarker.template.Configuration(VERSION_2_3_21);
    configuration.setDirectoryForTemplateLoading(applicationContext.getResource("classpath:template").getFile());
    configuration.setDefaultEncoding("UTF-8");
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    return configuration;
  }

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
