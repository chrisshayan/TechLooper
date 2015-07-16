package com.techlooper.config;

import com.techlooper.converter.LocaleConverter;
import com.techlooper.service.ProjectService;
import com.techlooper.service.impl.ProjectServiceImpl;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.social.facebook.api.FacebookProfile;

/**
 * Created by NguyenDangKhoa on 7/13/15.
 */
@Configuration
@PropertySources({@PropertySource("classpath:techlooper.properties")})
public class ProjectServiceConfigurationTest {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
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

    @Bean
    public ProjectService projectService() {
        return new ProjectServiceImpl();
    }
}
