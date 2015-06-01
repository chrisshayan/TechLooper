package com.techlooper.config;

import com.techlooper.service.SuggestionService;
import com.techlooper.service.impl.SuggestionServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;


/**
 * Created by phuonghqh on 10/20/14.
 */

@Configuration
@PropertySources({
        @PropertySource("classpath:techlooper.properties"),
        @PropertySource("classpath:secret.properties")})
@Import(CouchbaseConfiguration.class)
public class SkillConfigurationTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public SuggestionService skillSuggestionService() {
        return new SuggestionServiceImpl();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
