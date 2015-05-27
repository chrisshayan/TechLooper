package com.techlooper.config;

import com.techlooper.converter.ListCSVStringConverter;
import com.techlooper.converter.LocaleConverter;
import com.techlooper.converter.ProfileNameConverter;
import com.techlooper.entity.*;
import com.techlooper.model.UserInfo;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VnwJobAlert;
import com.techlooper.model.VnwJobAlertRequest;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.dozer.loader.api.TypeMappingOptions;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.google.api.plus.Person;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

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

  @Value("${mail.host}")
  private String mailHost;

  @Value("${mail.form}")
  private String mailForm;

  @Value("${mail.username}")
  private String mailUsername;

  @Value("${mail.password}")
  private String mailPassword;

  @Value("${mail.reply_to}")
  private String mailReplyTo;

  @Value("${mail.port}")
  private Integer mailPort;

  @Value("${mail.citibank.cc_promotion.to}")
  private String mailCitibankCreditCardPromotionTo;

  @Value("${mail.citibank.cc_promotion.subject}")
  private String mailCitibankCreditCardPromotionSubject;

  @Resource
  private freemarker.template.Configuration freemakerConfig;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public CacheManager cacheManager() {
    CompositeCacheManager manager = new CompositeCacheManager();
    manager.setCacheManagers(Arrays.asList(
      new ConcurrentMapCacheManager("SOCIAL_CONFIG"),
      new ConcurrentMapCacheManager("COMMON_TERM"),
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

        mapping(SalaryReview.class, VNWJobSearchRequest.class)
          .fields("jobLevelIds", "jobLevel")
          .fields("jobCategories", "jobCategories", FieldsMappingOptions.customConverter(ListCSVStringConverter.class))
          .fields("netSalary", "jobSalary")
          .fields("locationId", "jobLocation");

        mapping(VnwJobAlert.class, VnwJobAlertRequest.class)
          .fields("jobLocations", "locationId", FieldsMappingOptions.customConverter(ListCSVStringConverter.class))
          .fields("minSalary", "netSalary");
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

  @Bean
  public MailSender mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(mailHost);
//    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(mailPort);
    mailSender.setUsername(mailUsername);
    mailSender.setPassword(mailPassword);

    Properties javaMailProperties = new Properties();
    javaMailProperties.put("mail.smtp.auth", true);
    javaMailProperties.put("mail.smtp.starttls.enable", true);
    mailSender.setJavaMailProperties(javaMailProperties);
    return mailSender;
  }

  @Bean
  public SimpleMailMessage citibankCreditCardPromotionMailMessage() {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(mailForm);
    mailMessage.setTo(mailCitibankCreditCardPromotionTo);
    mailMessage.setReplyTo(mailReplyTo);
    mailMessage.setSubject(mailCitibankCreditCardPromotionSubject);
    return mailMessage;
  }

  @Bean
  public freemarker.template.Configuration freemakerConfig() throws IOException {
    freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_22);
    cfg.setDirectoryForTemplateLoading(new java.io.File(this.getClass().getClassLoader().getResource("/").getFile() + "template"));
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    return cfg;
  }

  @Bean
  @DependsOn("freemakerConfig")
  public Template citibankCreditCardPromotionTemplate() throws IOException {
    Template template = freemakerConfig.getTemplate("citibankCreditCardPromotion.ftl");
    return template;
  }
}
