package com.techlooper.config;

import freemarker.cache.MruCacheStorage;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.BeansException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

import static freemarker.template.Configuration.VERSION_2_3_21;

@Configuration
@ComponentScan(basePackages = "com.techlooper")
@PropertySources({
  @PropertySource("classpath:techlooper.properties"),
  @PropertySource("classpath:secret.properties")})
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching
@Import({ElasticsearchConfiguration.class})
public class CoreConfiguration implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public CacheManager cacheManager() {
    SimpleCacheManager cacheManager = new SimpleCacheManager();
    cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("default")));
    return cacheManager;
  }

  @Bean
  public freemarker.template.Configuration freemarkerConfiguration() throws IOException {
    freemarker.template.Configuration configuration = new freemarker.template.Configuration(VERSION_2_3_21);
    configuration.setDirectoryForTemplateLoading(applicationContext.getResource("classpath:template").getFile());
    configuration.setDefaultEncoding("UTF-8");
    configuration.setCacheStorage(new MruCacheStorage(100, 100));
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    return configuration;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
