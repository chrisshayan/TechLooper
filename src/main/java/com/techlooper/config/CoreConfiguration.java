package com.techlooper.config;

import com.techlooper.converter.LocaleConverter;
import com.techlooper.converter.ProfileNameConverter;
import com.techlooper.entity.*;
import com.techlooper.model.UserInfo;
import com.techlooper.repository.talentsearch.GithubTalentSearchRepository;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.dozer.loader.api.TypeMappingOptions;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.google.api.plus.Person;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
@ComponentScan(basePackages = "com.techlooper")
@PropertySources({
        @PropertySource("classpath:techlooper.properties"),
        @PropertySource("classpath:secret.properties")})
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching(proxyTargetClass = true)
public class CoreConfiguration {

    @Resource
    private Environment environment;

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
        dozerBeanMapper.addMapping(new BeanMappingBuilder() {
            protected void configure() {
                mapping(FacebookProfile.class, com.techlooper.entity.FacebookProfile.class)
                        .fields("locale", "locale", FieldsMappingOptions.customConverter(LocaleConverter.class));

                mapping(TwitterProfile.class, com.techlooper.entity.UserEntity.class, TypeMappingOptions.oneWay())
                        .fields("name", "firstName", FieldsMappingOptions.copyByReference())
                        .fields("screenName", "userName", FieldsMappingOptions.copyByReference());

                mapping(GitHubUserProfile.class, com.techlooper.entity.UserEntity.class, TypeMappingOptions.oneWay())
                        .fields("name", "firstName", FieldsMappingOptions.copyByReference())
                        .fields("email", "emailAddress", FieldsMappingOptions.copyByReference());

                mapping(Person.class, com.techlooper.entity.UserEntity.class, TypeMappingOptions.oneWay())
                        .fields("givenName", "firstName", FieldsMappingOptions.copyByReference())
                        .fields("familyName", "lastName", FieldsMappingOptions.copyByReference())
                        .fields("accountEmail", "emailAddress", FieldsMappingOptions.copyByReference())
                        .fields("imageUrl", "profileImageUrl", FieldsMappingOptions.copyByReference());

                mapping(com.techlooper.entity.FacebookProfile.class, com.techlooper.entity.UserEntity.class, TypeMappingOptions.oneWay())
                        .fields("email", "emailAddress", FieldsMappingOptions.copyByReference());

                mapping(LinkedInProfile.class, com.techlooper.entity.UserEntity.class, TypeMappingOptions.oneWay())
                        .fields("profilePictureUrl", "profileImageUrl", FieldsMappingOptions.copyByReference());

                mapping(UserEntity.class, UserInfo.class, TypeMappingOptions.oneWay())
                        .fields("profiles", "profileNames", FieldsMappingOptions.customConverter(ProfileNameConverter.class));

                mapping(UserInfo.class, UserEntity.class, TypeMappingOptions.oneWay())
                        .fields("profileNames", "profiles", FieldsMappingOptions.customConverter(ProfileNameConverter.class));

                mapping(UserEntity.class, VnwUserProfile.class).exclude("accessGrant");

            }
        });
        return dozerBeanMapper;
    }

    @Bean
    public TextEncryptor textEncryptor() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(environment.getProperty("core.textEncryptor.password"));
        return textEncryptor;
    }

    @Bean(name = "GITHUBTalentSearchRepository")
    public GithubTalentSearchRepository githubTalentSearchRepository() {
        return new GithubTalentSearchRepository();
    }
}
