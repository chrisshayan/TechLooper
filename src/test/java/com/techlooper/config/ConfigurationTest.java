package com.techlooper.config;

import com.techlooper.converter.LocaleConverter;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.repository.TechnicalTermRepository;
import com.techlooper.repository.talentsearch.query.GithubTalentSearchQuery;
import com.techlooper.repository.talentsearch.query.VietnamworksTalentSearchQuery;
import com.techlooper.service.*;
import com.techlooper.service.impl.*;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


/**
 * Created by phuonghqh on 10/20/14.
 */

@Configuration
@PropertySources({
        @PropertySource("classpath:techlooper.properties"),
        @PropertySource("classpath:secret.properties")})
@Import(CouchbaseConfiguration.class)
public class ConfigurationTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JobSearchAPIConfigurationRepository apiConfiguration() {
        return new JobSearchAPIConfigurationRepository();
    }

    @Bean
    public JobSearchService jobSearchService() {
        return new VietnamWorksJobSearchService();
    }

    @Bean
    public TechnicalTermRepository technicalTermRepository() {
        return new TechnicalTermRepository();
    }

    @Bean
    public JsonConfigRepository jsonConfigRepository() {
        return new JsonConfigRepository();
    }

    @Bean
    public JobQueryBuilder jobQueryBuilder() {
        return new JobQueryBuilderImpl();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public VietnamWorksUserService vietnamworksUserService() {
        return new VietnamWorksUserServiceImpl();
    }

    @Bean
    public Mapper dozerBeanMapper() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        BeanMappingBuilder builder = new BeanMappingBuilder() {
            protected void configure() {
                mapping(FacebookProfile.class, com.techlooper.entity.FacebookProfile.class).fields("locale", "locale", FieldsMappingOptions.customConverter(LocaleConverter.class));
            }
        };
        dozerBeanMapper.addMapping(builder);
        return dozerBeanMapper;
    }

    @Bean(name = "GITHUBTalentSearchDataProcessor")
    public GithubTalentSearchDataProcessor githubTalentSearchDataProcessor() {
        return new GithubTalentSearchDataProcessor();
    }

    @Bean(name = "VIETNAMWORKSTalentSearchDataProcessor")
    public VietnamworksTalentSearchDataProcessor vietnamworksTalentSearchDataProcessor() {
        return new VietnamworksTalentSearchDataProcessor();
    }

    @Bean(name = "GITHUBTalentSearchQuery")
    public GithubTalentSearchQuery githubTalentSearchQuery() {
        return new GithubTalentSearchQuery();
    }

    @Bean(name = "VIETNAMWORKSTalentSearchQuery")
    public VietnamworksTalentSearchQuery vietnamworksTalentSearchQuery() {
        return new VietnamworksTalentSearchQuery();
    }

    @Bean
    public TextEncryptor textEncryptor() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(environment.getProperty("core.textEncryptor.password"));
        return textEncryptor;
    }

    @Bean
    public UserEvaluationService userEvaluationService() {
        return new UserEvaluationServiceImpl();
    }

    @Bean
    public JobStatisticService jobStatisticService() {
        return new VietnamWorksJobStatisticService();
    }

    @Bean
    public CompanyService companyService() {
        return new CompanyServiceImpl();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
