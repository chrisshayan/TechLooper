package com.techlooper.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.techlooper.converter.ListCSVStringConverter;
import com.techlooper.converter.LocaleConverter;
import com.techlooper.converter.ProfileNameConverter;
import com.techlooper.dto.WebinarInfoDto;
import com.techlooper.entity.*;
import com.techlooper.model.*;
import com.techlooper.repository.JsonConfigRepository;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.dozer.loader.api.TypeMappingOptions;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
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
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;

@Configuration
@ComponentScan(basePackages = "com.techlooper")
@PropertySources({
  @PropertySource("classpath:techlooper.properties"),
  @PropertySource("classpath:secret.properties")})
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching(proxyTargetClass = true)
public class CoreConfiguration implements ApplicationContextAware {

  @Resource
  private Environment environment;

  @Value("${mail.techlooper.form}")
  private String mailTechlooperForm;

  @Value("${mail.techlooper.reply_to}")
  private String mailTechlooperReplyTo;

  @Value("${mail.form}")
  private String mailForm;

  @Value("${mail.reply_to}")
  private String mailReplyTo;

  @Value("${mail.citibank.cc_promotion.to}")
  private String mailCitibankCreditCardPromotionTo;

  @Value("${mail.citibank.cc_promotion.subject}")
  private String mailCitibankCreditCardPromotionSubject;

  @Value("classpath:vnwConfig.json")
  private org.springframework.core.io.Resource vnwConfigRes;

  @Value("classpath:google-auth/techLooper.p12")
  private org.springframework.core.io.Resource googleApiAuthResource;
//  @Value("classpath:google-auth")
//  private org.springframework.core.io.Resource googleApiAuthResource;

  private ApplicationContext applicationContext;

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setFileEncoding("UTF-8");
    return configurer;
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

        mapping(SalaryReviewEntity.class, VNWJobSearchRequest.class)
          .fields("jobLevelIds", "jobLevel")
          .fields("jobCategories", "jobCategories", FieldsMappingOptions.customConverter(ListCSVStringConverter.class))
          .fields("netSalary", "jobSalary")
          .fields("locationId", "jobLocation");

        mapping(VnwJobAlert.class, VnwJobAlertRequest.class)
          .fields("jobLocations", "locationId", FieldsMappingOptions.customConverter(ListCSVStringConverter.class))
          .fields("minSalary", "netSalary");

        mapping(WebinarInfoDto.class, WebinarEntity.class, TypeMappingOptions.oneWay())
          .exclude("createdDateTime");
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
  public MimeMessage salaryReviewMailMessage(JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {
    MimeMessage mailMessage = mailSender.createMimeMessage();
    mailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
    mailMessage.setFrom(new InternetAddress(mailTechlooperForm, "TechLooper", "UTF-8"));
    return mailMessage;
  }

  @Bean
  public MimeMessage getPromotedMailMessage(JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {
    MimeMessage mailMessage = mailSender.createMimeMessage();
    mailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
    mailMessage.setFrom(new InternetAddress(mailTechlooperForm, "TechLooper", "UTF-8"));
    return mailMessage;
  }

  @Bean
  public MimeMessage postChallengeMailMessage(JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {
    MimeMessage mailMessage = mailSender.createMimeMessage();
    mailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
    mailMessage.setFrom(new InternetAddress(mailTechlooperForm, "TechLooper", "UTF-8"));
    return mailMessage;
  }

  @Bean
  public MimeMessage applyJobMailMessage(JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {
    MimeMessage mailMessage = mailSender.createMimeMessage();
    mailMessage.setFrom(new InternetAddress(mailTechlooperForm, "TechLooper", "UTF-8"));
    return mailMessage;
  }

  @Bean
  public MimeMessage jobAlertMailMessage(JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {
    MimeMessage mailMessage = mailSender.createMimeMessage();
    mailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
    mailMessage.setFrom(new InternetAddress(mailTechlooperForm, "TechLooper", "UTF-8"));
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
  public Template getPromotedTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("getPromoted.en.ftl");
    return template;
  }

  @Bean
  public Template getPromotedTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("getPromoted.vi.ftl");
    return template;
  }

  @Bean
  public Template postChallengeMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("postChallenge.en.ftl");
    return template;
  }

  @Bean
  public Template postChallengeMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("postChallenge.vi.ftl");
    return template;
  }

  @Bean
  public Template confirmUserJoinChallengeMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("confirmUserJoinChallenge.en.ftl");
    return template;
  }

  @Bean
  public Template confirmUserJoinChallengeMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("confirmUserJoinChallenge.vi.ftl");
    return template;
  }

  @Bean
  public Template alertEmployerChallengeMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("alertEmployerChallenge.en.ftl");
    return template;
  }

  @Bean
  public Template alertEmployerChallengeMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("alertEmployerChallenge.vi.ftl");
    return template;
  }

  @Bean
  public Template alertJobSeekerApplyJobMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("alertJobSeekerApplyJob.en.ftl");
    return template;
  }

  @Bean
  public Template alertJobSeekerApplyJobMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("alertJobSeekerApplyJob.vi.ftl");
    return template;
  }

  @Bean
  public Template alertEmployerApplyJobMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("alertEmployerApplyJob.en.ftl");
    return template;
  }

  @Bean
  public Template alertEmployerApplyJobMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("alertEmployerApplyJob.vi.ftl");
    return template;
  }

  @Bean
  public Template alertEmployerPostJobMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("alertEmployerPostJob.vi.ftl");
    return template;
  }

  @Bean
  public Template alertEmployerPostJobMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("alertEmployerPostJob.en.ftl");
    return template;
  }

  @Bean
  public Template alertTechloopiesPostJobMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("alertTechloopiesApplyJob.en.ftl");
    return template;
  }

  @Bean
  public Template jobAlertMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("jobAlert.en.ftl");
    return template;
  }

  @Bean
  public Template jobAlertMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
    Template template = freemakerConfig.getTemplate("jobAlert.vi.ftl");
    return template;
  }

  @Bean
  public JsonNode vietnamworksConfiguration() throws IOException {
    return new ObjectMapper().readTree(vnwConfigRes.getInputStream());
  }

  @Bean
  @DependsOn("jsonConfigRepository")
  public Calendar googleCalendar() throws GeneralSecurityException, IOException {
    JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

    SocialConfig googleConfig = applicationContext.getBean(JsonConfigRepository.class).getSocialConfig().stream()
      .filter(socialConfig -> socialConfig.getProvider() == SocialProvider.GOOGLE)
      .findFirst().get();

//    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(transport, jsonFactory,
//      googleConfig.getApiKey(), googleConfig.getSecretKey(),
//      Collections.singleton(CalendarScopes.CALENDAR))
//      .setDataStoreFactory(new FileDataStoreFactory(googleApiAuthResource.getFile()))
//      .setAccessType("offline").build();
//
//    Credential credential = flow.loadCredential("techlooper");

//    String emailAddress = "339114452335-ndprm0gt8gsl86ek5ganv7767mg2gc89@developer.gserviceaccount.com";
    JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    GoogleCredential credential = new GoogleCredential.Builder()
      .setTransport(httpTransport)
      .setJsonFactory(JSON_FACTORY)
      .setServiceAccountId(googleConfig.getServiceAccountEmail())
      .setServiceAccountPrivateKeyFromP12File(googleApiAuthResource.getFile())
      .setServiceAccountScopes(Collections.singleton(CalendarScopes.CALENDAR))
      .setServiceAccountUser("techlooperawesome@gmail.com")
      .build();

//    boolean bool = credential.refreshToken();
//    String token = credential.getAccessToken();
//    System.out.println(bool);
//    System.out.println(token);

    return new Calendar.Builder(transport, jsonFactory, credential)
      .setApplicationName("Techlooper").build();

//    Event event = new Event()
//      .setSummary("Google I/O 2015")
//      .setLocation("800 Howard St., San Francisco, CA 94103")
//      .setDescription("A chance to hear more about Google's developer products.");
//
//    DateTime startDateTime = new DateTime("2015-09-28T09:00:00-07:00");
//    EventDateTime start = new EventDateTime()
//      .setDateTime(startDateTime)
//      .setTimeZone("America/Los_Angeles");
//    event.setStart(start);
//
//    DateTime endDateTime = new DateTime("2015-09-28T17:00:00-07:00");
//    EventDateTime end = new EventDateTime()
//      .setDateTime(endDateTime)
//      .setTimeZone("America/Los_Angeles");
//    event.setEnd(end);
//
//    EventAttendee[] attendees = new EventAttendee[]{
//      new EventAttendee().setEmail("phuonghqh@gmail.com"),
//      new EventAttendee().setEmail("thu.hoang@navigosgroup.com"),
//    };
//    event.setAttendees(Arrays.asList(attendees));
//
//    String calendarId = "techlooperawesome@gmail.com";
//    event = calendar.events().insert(calendarId, event).setSendNotifications(true).execute();
//    System.out.printf("Event created: %s\n", event.getHtmlLink());
//    event.getHangoutLink()
//    return calendar;
  }

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    this.applicationContext.getEnvironment().acceptsProfiles(environment.getProperty("spring.profiles.active"));
  }
}
