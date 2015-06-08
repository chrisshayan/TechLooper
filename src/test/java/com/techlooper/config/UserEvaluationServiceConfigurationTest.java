package com.techlooper.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.converter.LocaleConverter;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.SalaryReviewService;
import com.techlooper.service.UserEvaluationService;
import com.techlooper.service.impl.JobQueryBuilderImpl;
import com.techlooper.service.impl.SalaryReviewServiceImpl;
import com.techlooper.service.impl.UserEvaluationServiceImpl;
import com.techlooper.service.impl.VietnamWorksJobSearchService;
import freemarker.template.Template;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URISyntaxException;

@Configuration
@PropertySources({@PropertySource("classpath:techlooper.properties"),
        @PropertySource("classpath:secret.properties")})
public class UserEvaluationServiceConfigurationTest {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public UserEvaluationService userEvaluationService() {
        return new UserEvaluationServiceImpl();
    }

    @Bean
    public JobSearchService jobSearchService() {
        return new VietnamWorksJobSearchService();
    }

    @Bean
    public JobQueryBuilder jobQueryBuilder() {
        return new JobQueryBuilderImpl();
    }

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        return mailSender;
    }

    @Bean
    public freemarker.template.Configuration freemakerConfig() throws IOException, URISyntaxException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_22);
        return cfg;
    }

    @Bean
    public Template salaryReviewReportTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
        return null;
    }

    @Bean
    public Template salaryReviewReportTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
        return null;
    }

    @Bean
    public JsonNode vietnamworksConfiguration() throws IOException {
        return null;
    }

    @Bean
    public MimeMessage salaryReviewMailMessage(JavaMailSender mailSender) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        return mailMessage;
    }

    @Bean
    public SalaryReviewService salaryReviewService() {
        return new SalaryReviewServiceImpl();
    }

    @Bean
    public JobSearchAPIConfigurationRepository apiConfiguration() {
        return new JobSearchAPIConfigurationRepository();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JsonConfigRepository jsonConfigRepository() {
        return new JsonConfigRepository();
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

}
