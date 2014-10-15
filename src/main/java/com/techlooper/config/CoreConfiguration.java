package com.techlooper.config;

import freemarker.cache.MruCacheStorage;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.Arrays;

import static freemarker.template.Configuration.VERSION_2_3_21;

@Configuration
@ComponentScan(basePackages = "com.techlooper")
// The envTarget variable can be set in the OS/environment or as a parameter to
// the JVM command line: -DenvTarget=dev
@PropertySource("classpath:${envTarget:techlooper}.properties")
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching
@Import({ElasticsearchConfiguration.class, IntegrationConfiguration.class})
public class CoreConfiguration {

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
    configuration.setDirectoryForTemplateLoading(new ClassPathResource("template").getFile());
    configuration.setDefaultEncoding("UTF-8");
    configuration.setCacheStorage(new MruCacheStorage(100, 100));
    configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    return configuration;
  }
}
