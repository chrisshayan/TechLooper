package com.techlooper.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techlooper.converter.ListCSVStringConverter;
import com.techlooper.converter.LocaleConverter;
import com.techlooper.converter.ProfileNameConverter;
import com.techlooper.entity.*;
import com.techlooper.model.UserInfo;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VnwJobAlert;
import com.techlooper.model.VnwJobAlertRequest;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.util.RestTemplateUtils;
import freemarker.template.Template;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.google.api.plus.Person;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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

  @Value("${mail.form}")
  private String mailForm;

  @Value("${mail.reply_to}")
  private String mailReplyTo;

  @Value("${mail.citibank.cc_promotion.to}")
  private String mailCitibankCreditCardPromotionTo;

  @Value("${mail.citibank.cc_promotion.subject}")
  private String mailCitibankCreditCardPromotionSubject;

//  @Value("${mail.salaryReview.subject}")
//  private String salaryReviewSubject;

//  @Resource
//  private JobSearchAPIConfigurationRepository jobSearchAPIConfigurationRepository;

  @Value("classpath:vnwConfig.json")
  private org.springframework.core.io.Resource vnwConfigRes;

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
  public SimpleMailMessage citibankCreditCardPromotionMailMessage() {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(mailForm);
    mailMessage.setTo(mailCitibankCreditCardPromotionTo);
    mailMessage.setReplyTo(mailReplyTo);
    mailMessage.setSubject(mailCitibankCreditCardPromotionSubject);
    return mailMessage;
  }

  @Bean
  public freemarker.template.Configuration freemakerConfig() throws IOException, URISyntaxException {
    freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_22);
    cfg.setDirectoryForTemplateLoading(Paths.get(this.getClass().getClassLoader().getResource("/template").toURI()).toFile());
    cfg.setDefaultEncoding("UTF-8");
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    return cfg;
  }

  @Bean
  public Template citibankCreditCardPromotionTemplate(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("citibankCreditCardPromotion.ftl");
    return template;
  }

  @Bean
  public Map<Long, String> locationMap() {
    Map<Long, String> locations = new HashMap<>();
    locations.put(29L, "Ho Chi Minh");
    locations.put(24L, "Ha Noi");
    return locations;
  }

  @Bean
  public MimeMessage salaryReviewMailMessage(JavaMailSender mailSender) throws MessagingException {
    MimeMessage mailMessage = mailSender.createMimeMessage();
    mailMessage.setReplyTo(InternetAddress.parse(mailReplyTo));
//    mailMessage.setSubject(salaryReviewSubject);
    mailMessage.setFrom(InternetAddress.parse(mailForm)[0]);
    return mailMessage;
  }

  @Bean
  public Template salaryReviewReportTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("salaryReviewReport.en.ftl");
    return template;
  }

  @Bean
  public Template salaryReviewReportTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("salaryReviewReport.vi.ftl");
    return template;
  }

  @Bean
  public JsonNode vietnamworksConfiguration() throws IOException {
    return new ObjectMapper().readTree(vnwConfigRes.getInputStream());
//    HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(
//      MediaType.APPLICATION_JSON, jobSearchAPIConfigurationRepository.getApiKeyName(), jobSearchAPIConfigurationRepository.getApiKeyValue(), null);
//    return restTemplate().exchange(jobSearchAPIConfigurationRepository.getConfigurationUrl(), HttpMethod.GET, requestEntity, JsonNode.class).getBody();
  }
}
