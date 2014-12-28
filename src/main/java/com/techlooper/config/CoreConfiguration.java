package com.techlooper.config;

import com.techlooper.converter.ProfileNameConverter;
import com.techlooper.entity.UserEntity;
import com.techlooper.model.UserInfo;
import com.techlooper.converter.LocaleConverter;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.dozer.loader.api.TypeMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.util.*;

@Configuration
@ComponentScan(basePackages = "com.techlooper")
@PropertySources({
  @PropertySource("classpath:techlooper.properties"),
  @PropertySource("classpath:secret.properties")})
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching(proxyTargetClass = true)
public class CoreConfiguration {

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public CacheManager cacheManager() {
    CompositeCacheManager manager = new CompositeCacheManager();
    manager.setCacheManagers(Arrays.asList(
      new ConcurrentMapCacheManager("SOCIAL_CONFIG"),
      new ConcurrentMapCacheManager("SKILL_CONFIG")));
    return manager;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public MultipartResolver multipartResolver() {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize(500000);
    return multipartResolver;
  }

  @Bean
  public Mapper dozerBeanMapper() {
    DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
    BeanMappingBuilder builder = new BeanMappingBuilder() {
      protected void configure() {
        mapping(FacebookProfile.class, com.techlooper.entity.FacebookProfile.class).fields("locale", "locale", FieldsMappingOptions.customConverter(LocaleConverter.class));
        mapping(UserEntity.class, UserInfo.class, TypeMappingOptions.oneWay()).fields("profiles", "profileNames", FieldsMappingOptions.customConverter(ProfileNameConverter.class));
      }
    };
    dozerBeanMapper.addMapping(builder);
    return dozerBeanMapper;
  }

  @Bean
  public PasswordEncryptor passwordEncryptor() {
    return new StrongPasswordEncryptor();
  }

}
