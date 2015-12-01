package com.techlooper.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.techlooper.converter.ListCSVStringConverter;
import com.techlooper.converter.LocaleConverter;
import com.techlooper.converter.ProfileNameConverter;
import com.techlooper.cron.*;
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
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
        return new RestTemplate(clientHttpRequestFactory());
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

                mapping(ScrapeJobEntity.class, VNWJobSearchResponseDataItem.class)
                        .fields("jobTitle", "title")
                        .fields("jobTitleUrl", "url")
                        .fields("companyLogoUrl", "logoUrl")
                        .fields("createdDateTime", "postedOn");

                mapping(ScrapeJobEntity.class, JobResponse.class)
                        .fields("jobTitle", "title")
                        .fields("jobTitleUrl", "url")
                        .fields("createdDateTime", "postedOn")
                        .fields("companyLogoUrl", "logoUrl");

                mapping(ChallengeEntity.class, ChallengeDto.class)
                        .fields("startDateTime", "startDate")
                        .fields("submissionDateTime", "submissionDate")
                        .fields("registrationDateTime", "registrationDate")
                        .fields("ideaSubmissionDateTime", "ideaSubmissionDate")
                        .fields("uxSubmissionDateTime", "uxSubmissionDate")
                        .fields("prototypeSubmissionDateTime", "prototypeSubmissionDate");

                mapping(ChallengeRegistrantDto.class, ChallengeRegistrantEntity.class, TypeMappingOptions.oneWay())
                        .exclude("activePhase");

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
    public MimeMessage alertEventOrganiserMailMessage(JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
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
    public Template notifyChallengeTimelineMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("notifyChallengeTimeline.vi.ftl");
        return template;
    }

    @Bean
    public Template notifyChallengeTimelineMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("notifyChallengeTimeline.en.ftl");
        return template;
    }

    @Bean
    public Template postChallengeUpdateMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("updateChallenge.en.ftl");
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
    public Template alertEventOrganiserMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("webinar.en.ftl");
        return template;
    }

    @Bean
    public Template alertEventOrganiserMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("webinar.vi.ftl");
        return template;
    }

    @Bean
    public Template onBoardingMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("onboarding.en.ftl");
        return template;
    }

    @Bean
    public Template onBoardingMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("onboarding.vi.ftl");
        return template;
    }

    @Bean
    public Template dailyChallengeSummaryMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("challengeDailySummary.en.ftl");
        return template;
    }

    @Bean
    public Template dailyChallengeSummaryMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("challengeDailySummary.vi.ftl");
        return template;
    }

    @Bean
    public JsonNode vietnamworksConfiguration() throws IOException {
        return new ObjectMapper().readTree(vnwConfigRes.getInputStream());
    }

    @Bean
    @DependsOn("jsonConfigRepository")
    public SocialConfig googleSocialConfig() {
        return applicationContext.getBean(JsonConfigRepository.class).getSocialConfig().stream()
                .filter(socialConfig -> socialConfig.getProvider() == SocialProvider.GOOGLE)
                .findFirst().get();
    }

    @Bean
    public Calendar googleCalendar() throws GeneralSecurityException, IOException {
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        SocialConfig googleConfig = googleSocialConfig();
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId(googleConfig.getServiceAccountEmail())
                .setServiceAccountPrivateKeyFromP12File(googleApiAuthResource.getFile())
                .setServiceAccountScopes(Collections.singleton(CalendarScopes.CALENDAR))
                .setServiceAccountUser(googleConfig.getCalendarOwner())
                .build();

        return new Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName("Techlooper").build();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.applicationContext.getEnvironment().acceptsProfiles(environment.getProperty("spring.profiles.active"));
    }

    @Bean
    public MimeMessage fromTechlooperMailMessage(JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        mailMessage.setFrom(new InternetAddress(mailTechlooperForm, "TechLooper", "UTF-8"));
        mailMessage.setReplyTo(InternetAddress.parse(mailTechlooperReplyTo));
        return mailMessage;
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }

    @Bean
    public Template challengeEmployerMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("challengeEmployerEmail.en.ftl");
        return template;
    }

    @Bean
    public Template challengeEmployerMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("challengeEmployerEmail.vi.ftl");
        return template;
    }

    @Bean
    public ChallengeTimelineNotifier challengeTimelineNotifier() {
        return new ChallengeTimelineNotifier();
    }

    @Bean
    public DailyChallengeSummaryEmailSender dailyChallengeSummaryEmailSender() {
        return new DailyChallengeSummaryEmailSender();
    }

    @Bean
    public JobAlertEmailSender jobAlertEmailSender() {
        return new JobAlertEmailSender();
    }

    @Bean
    public VietnamworksJobImporter vietnamworksJobImporter() {
        return new VietnamworksJobImporter();
    }

    @Bean
    public MimeMessage confirmUserSubmissionMailMessage(JavaMailSender mailSender) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        mailMessage.setFrom(new InternetAddress(mailTechlooperForm, "TechLooper", "UTF-8"));
        return mailMessage;
    }

    @Bean
    public Template confirmUserSubmissionMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("confirmUserSubmission.en.ftl");
        return template;
    }

    @Bean
    public Template confirmUserSubmissionMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("confirmUserSubmission.vi.ftl");
        return template;
    }

    @Bean
    public ChallengeSubmissionNotifier challengeSubmissionNotifier() {
        return new ChallengeSubmissionNotifier();
    }

    @Bean
    public Template notifyChallengeSubmissionMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("notifyChallengeSubmission.en.ftl");
        return template;
    }

    @Bean
    public Template notifyChallengeSubmissionMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("notifyChallengeSubmission.vi.ftl");
        return template;
    }

    @Bean
    public ChallengePhaseClosedNotifier challengePhaseClosedNotifier() {
        return new ChallengePhaseClosedNotifier();
    }

    @Bean
    public Template notifyChallengePhaseClosedMailTemplateEn(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("notifyChallengePhaseClosed.en.ftl");
        return template;
    }

    @Bean
    public Template notifyChallengePhaseClosedMailTemplateVi(freemarker.template.Configuration freemakerConfig) throws IOException {
        Template template = freemakerConfig.getTemplate("notifyChallengePhaseClosed.vi.ftl");
        return template;
    }
}