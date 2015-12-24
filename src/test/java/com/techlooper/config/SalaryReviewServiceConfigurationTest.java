package com.techlooper.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techlooper.converter.LocaleConverter;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.service.SalaryReviewService;
import com.techlooper.service.impl.SalaryReviewServiceImpl;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by NguyenDangKhoa on 7/13/15.
 */
@Configuration
@PropertySources({@PropertySource("classpath:techlooper.properties")})
public class SalaryReviewServiceConfigurationTest {

    @Value("classpath:vnwConfig.json")
    private org.springframework.core.io.Resource vnwConfigRes;

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
    public JsonNode vietnamworksConfiguration() throws IOException {
        return new ObjectMapper().readTree(vnwConfigRes.getInputStream());
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(25);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.transport.protocol", "smtp");
        mailSender.setJavaMailProperties(javaMailProperties);
        return mailSender;
    }

    @Bean
    public MimeMessage salaryReviewMailMessage(JavaMailSender mailSender) {
        return mailSender.createMimeMessage();
    }

    @Bean
    public SalaryReviewService salaryReviewService() {
        return new SalaryReviewServiceImpl();
    }

    @Bean
    public JobSearchAPIConfigurationRepository apiConfiguration() {
        return null;
    }

    @Bean
    public RestTemplate restTemplate() {
        return null;
    }
}
