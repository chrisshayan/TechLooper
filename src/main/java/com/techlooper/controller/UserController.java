package com.techlooper.controller;

import com.techlooper.dto.*;
import com.techlooper.entity.*;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.entity.vnw.dto.VnwUserDto;
import com.techlooper.model.*;
import com.techlooper.service.*;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.dozer.Mapper;
import org.jasypt.util.text.TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * Created by phuonghqh on 12/23/14.
 */
@RestController
public class UserController {

    private final int MAX_NUMBER_OF_ITEMS_DISPLAY = 3;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private UserService userService;

    @Resource
    private TextEncryptor textEncryptor;

    @Resource
    private UserEvaluationService userEvaluationService;

    @Resource
    private PromotionService promotionService;

    @Resource
    private CurrencyService currencyService;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private SalaryReviewService salaryReviewService;

    @Resource
    private JobStatisticService jobStatisticService;

    @Resource
    private ChallengeService challengeService;

    @Value("${mail.citibank.title.vn}")
    private String citibankTitleVn;

    @Value("${mail.citibank.title.en}")
    private String citibankTitleEn;

    @Value("classpath:braille.txt")
    private org.springframework.core.io.Resource brailleTextFile;

    @Resource
    private EmployerService employerService;

    @Resource
    private WebinarService webinarService;

    @Resource
    private ProjectService projectService;

    @Resource
    private JobAggregatorService jobAggregatorService;

    @Resource
    private EmailService emailService;

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/api/users/add", method = RequestMethod.POST)
    public void save(@RequestBody UserImportData userImportData, HttpServletResponse httpServletResponse) {
        SocialProvider provider = userImportData.getCrawlerSource();
        UserImportDataProcessor dataProcessor = applicationContext.getBean(provider + "UserImportDataProcessor", UserImportDataProcessor.class);
        // process raw user data before import into ElasticSearch
        List<UserImportEntity> userImportEntities = dataProcessor.process(Arrays.asList(userImportData));
        httpServletResponse.setStatus(userService.addCrawledUser(userImportEntities.get(0), provider) ?
                HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_NOT_ACCEPTABLE);
    }

    @RequestMapping(value = "/api/users/addAll", method = RequestMethod.POST)
    public void saveAll(@RequestBody List<UserImportData> users, HttpServletResponse httpServletResponse) {
        if (!users.isEmpty()) {
            SocialProvider provider = users.get(0).getCrawlerSource();
            UserImportDataProcessor dataProcessor = applicationContext.getBean(provider + "UserImportDataProcessor", UserImportDataProcessor.class);
            // process raw user data before import into ElasticSearch
            List<UserImportEntity> userImportEntities = dataProcessor.process(users);
            httpServletResponse.setStatus(userService.addCrawledUserAll(userImportEntities, provider, UpdateModeEnum.MERGE) == users.size() ?
                    HttpServletResponse.SC_NO_CONTENT : HttpServletResponse.SC_NOT_ACCEPTABLE);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }

//    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
//    public List<FieldError> save(@RequestBody @Valid UserInfo userInfo, BindingResult result, HttpServletResponse httpServletResponse) {
//        if (result.getFieldErrorCount() > 0) {
//            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        } else {
//            userService.registerVietnamworksAccount(userInfo);
//            userService.save(userInfo);
//        }
//        return result.getFieldErrors();
//    }

    @RequestMapping(value = "/api/user/findTalent", method = RequestMethod.POST)
    public TalentSearchResponse findTalent(@RequestBody(required = false) TalentSearchRequest param, HttpServletResponse httpServletResponse) {
        return userService.findTalent(param);
    }

//    @RequestMapping(value = "/api/user/register", method = RequestMethod.POST)
//    public List<FieldError> registerUser(@RequestBody @Valid UserInfo userInfo, BindingResult result, HttpServletResponse httpServletResponse) {
//        if (result.getFieldErrorCount() > 0) {
//            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        } else {
//            userService.registerUser(userInfo);
//        }
//        return result.getFieldErrors();
//    }

//    @SendToUser("/queue/info")
//    @MessageMapping("/user/findByKey")
//    @RequestMapping(value = "/user/findByKey", method = RequestMethod.POST)
//    public UserInfo getUserInfo(@CookieValue("techlooper.key") String techlooperKey/*, @DestinationVariable String username */) {
//        UserInfo userInfo = userService.findUserInfoByKey(techlooperKey);
//        userInfo.getLoginSource();
//        return userInfo;
//    }

    @RequestMapping(value = "/user/verifyUserLogin", method = RequestMethod.POST)
    public void verifyUserLogin(@RequestBody SocialRequest searchRequest, @CookieValue("techlooper.key") String techlooperKey, HttpServletResponse httpServletResponse) {
        if (!textEncryptor.encrypt(searchRequest.getEmailAddress()).equals(techlooperKey)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @RequestMapping(value = "/salaryReview", method = RequestMethod.POST)
    public SalaryReviewDto reviewSalary(@RequestBody SalaryReviewEntity salaryReviewEntity) {
        userEvaluationService.reviewSalary(salaryReviewEntity);
        SalaryReviewDto salaryReviewDto = dozerMapper.map(salaryReviewEntity, SalaryReviewDto.class);
        salaryReviewDto.setUsdToVndRate(currencyService.usdToVndRate());
//        get top 3 similar salary reviews
        SimilarSalaryReviewRequest request = dozerMapper.map(salaryReviewDto, SimilarSalaryReviewRequest.class);
        List<SimilarSalaryReview> similarSalaryReviews = salaryReviewService.getSimilarSalaryReview(request);
        salaryReviewDto.setSimilarSalaryReviews(similarSalaryReviews);
        return salaryReviewDto;
    }

    @RequestMapping(value = "/salaryReview/{id}", method = RequestMethod.GET)
    public SalaryReviewDto getReviewSalary(@PathVariable("id") String id) {
        return userService.findSalaryReviewById(id);
    }

    @RequestMapping(value = "/saveSalaryReviewSurvey", method = RequestMethod.POST)
    public void saveSurvey(@RequestBody SalaryReviewSurvey salaryReviewSurvey, HttpServletResponse httpServletResponse) {
        boolean isSaved = userEvaluationService.saveSalaryReviewSurvey(salaryReviewSurvey);
        if (isSaved) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @RequestMapping(value = "/priceJob", method = RequestMethod.POST)
    public PriceJobEntity priceJob(@RequestBody PriceJobEntity priceJobEntity) {
        userEvaluationService.priceJob(priceJobEntity);
        return priceJobEntity;
    }

    @RequestMapping(value = "/savePriceJobSurvey", method = RequestMethod.POST)
    public void savePriceJobSurvey(@RequestBody PriceJobSurvey priceJobSurvey, HttpServletResponse httpServletResponse) {
        boolean isSaved = userEvaluationService.savePriceJobSurvey(priceJobSurvey);
        if (isSaved) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @RequestMapping("promotion/citibank/creditCard")
    public void placeCitibankCreditCardPromotion(@Valid @RequestBody CitibankCreditCardPromotion citibankCreditCardPromotion) throws IOException, TemplateException {
        promotionService.placePromotion(citibankCreditCardPromotion);
    }

    @RequestMapping(value = "salaryReview/placeSalaryReviewReport", method = RequestMethod.POST)
    public void placeSalaryReviewReport(@Valid @RequestBody EmailRequest emailRequest) throws TemplateException, IOException, MessagingException {
        salaryReviewService.sendSalaryReviewReportEmail(emailRequest);
    }

    @MessageMapping("/jobs/createJobAlert")
    public void createJobAlert(VnwJobAlertRequest vnwJobAlertRequest) {
        salaryReviewService.createVnwJobAlert(vnwJobAlertRequest);
    }

    @RequestMapping(value = "/promotion/citibank/title/{lang}", method = RequestMethod.GET)
    public String getCitiBankPromotionTitle(@PathVariable String lang) {
        return "vn".equalsIgnoreCase(lang) ? citibankTitleVn : citibankTitleEn;
    }

    @RequestMapping(value = "/getPromoted/email", method = RequestMethod.POST)
    public long sendTopDemandedSkillsEmail(@Valid @RequestBody GetPromotedEmailRequest emailRequest) throws MessagingException, IOException, TemplateException {
        long getPromotedId = salaryReviewService.saveGetPromotedInformation(emailRequest);

        if (getPromotedId != -1L && emailRequest.getHasResult()) {
            salaryReviewService.sendTopDemandedSkillsEmail(getPromotedId, emailRequest);
        }

        return getPromotedId;
    }

    @RequestMapping(value = "/getPromoted/survey", method = RequestMethod.POST)
    public long saveGetPromotedSurvey(@RequestBody GetPromotedSurveyRequest getPromotedSurveyRequest, HttpServletResponse httpServletResponse) {
        return salaryReviewService.saveGetPromotedSurvey(getPromotedSurveyRequest);
    }

    @RequestMapping(value = "/getPromotedResult/{id}", method = RequestMethod.GET)
    public GetPromotedEntity getPromotedResult(@PathVariable Long id) {
        return salaryReviewService.getPromotedEntity(id);
    }

    @RequestMapping(value = "/download/braille", method = RequestMethod.GET)
    public void getFile(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment;filename=braille.txt");
            IOUtils.copy(brailleTextFile.getInputStream(), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw ex;
        }
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @RequestMapping(value = "user/vnw-current", method = RequestMethod.GET)
    public VnwUserDto getVnwCurrentUser(HttpServletRequest servletRequest) {
        return userService.findVnwUserByUsername(servletRequest.getRemoteUser());
    }

    @RequestMapping(value = "/personalHomepage", method = RequestMethod.GET)
    public PersonalHomepageDto getPersonalHomepageDto() {
        PersonalHomepageDto personalHomepage = new PersonalHomepageDto();

        TermStatisticRequest termStatisticRequest = new TermStatisticRequest();
        try {
            termStatisticRequest.setTerm(jobStatisticService.getTechnicalTermHasTheMostJob().getKey());
            TermStatisticResponse termStatisticResponse =
                    jobStatisticService.generateTermStatistic(termStatisticRequest, HistogramEnum.ONE_YEAR);
            personalHomepage.setTermStatistic(termStatisticResponse);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        try {
            ChallengeDetailDto latestChallenge = challengeService.getTheLatestChallenge();
            latestChallenge.setNumberOfRegistrants(challengeService.getNumberOfRegistrants(latestChallenge.getChallengeId()));
            personalHomepage.setLatestChallenge(latestChallenge);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        try {
            personalHomepage.setLatestEvents(webinarService.listUpcomingWebinar());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        try {
            List<ProjectDto> latestProjects = projectService.listProject().stream().limit(MAX_NUMBER_OF_ITEMS_DISPLAY).collect(toList());
            personalHomepage.setLatestProjects(latestProjects);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        JobSearchCriteria criteria = new JobSearchCriteria();
        try {
            JobSearchResponse allJobSearchResponse = jobAggregatorService.findJob(criteria);
            personalHomepage.setTotalLatestJob(allJobSearchResponse.getTotalJob());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        criteria.setTopPriority(Boolean.FALSE);
        try {
            JobSearchResponse latestJobSearchResponse = jobAggregatorService.findJob(criteria);
            personalHomepage.setLatestJobs(latestJobSearchResponse.getJobs().stream().limit(MAX_NUMBER_OF_ITEMS_DISPLAY).collect(toSet()));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return personalHomepage;
    }

    @PreAuthorize("hasAnyAuthority('JOB_SEEKER', 'EMPLOYER')")
    @RequestMapping(value = "/user/current", method = RequestMethod.GET)
    public UserProfileDto getUserProfile(HttpServletRequest request) {
        LOGGER.debug("Reading current user profile info");
        Principal userPrincipal = request.getUserPrincipal();
        Object principal = ((UsernamePasswordAuthenticationToken) userPrincipal).getPrincipal();
        if (!(principal instanceof UserProfileDto)) {
            return dozerMapper.map(userService.findVnwUserByUsername(request.getRemoteUser()), UserProfileDto.class);
        }
        return (UserProfileDto) principal;
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @RequestMapping(value = "/user/employer/dashboard-info", method = RequestMethod.GET)
    public DashBoardInfo getEmployerDashboardInfo(HttpServletRequest request) {
        return employerService.getDashboardInfo(request.getRemoteUser());
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYER', 'JOB_SEEKER')")
    @RequestMapping(value = "/user/employer/webinar", method = RequestMethod.POST)
    public WebinarInfoDto createWebinar(@RequestBody WebinarInfoDto webinarInfoDto, HttpServletRequest request) throws IOException {
        Principal userPrincipal = request.getUserPrincipal();
        Object principal = ((UsernamePasswordAuthenticationToken) userPrincipal).getPrincipal();
        UserProfileDto organiser = (principal instanceof UserProfileDto) ? ((UserProfileDto) principal) :
                dozerMapper.map(getVnwCurrentUser(request), UserProfileDto.class);
        return webinarService.createWebinarInfo(webinarInfoDto, organiser);
    }

    @RequestMapping(value = "user/webinars", method = RequestMethod.GET)
    public Collection<WebinarInfoDto> findAvailableWebinars() {
        return webinarService.findAllWebinars();
    }

    @RequestMapping(value = "user/webinar/{id}", method = RequestMethod.GET)
    public WebinarInfoDto findWebinarById(@PathVariable Long id) {
        return webinarService.findWebinarById(id);
    }

    @RequestMapping(value = "user/webinar/join", method = RequestMethod.POST)
    public WebinarInfoDto joinWebinar(@RequestBody JoinBySocialDto joinBySocialDto) throws Exception {
        return webinarService.joinWebinar(joinBySocialDto);
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "user/challengeRegistrantNames/{challengeId}/{now}", method = RequestMethod.GET)
    public List<String> getDailyChallengeRegistrantNames(HttpServletRequest request, HttpServletResponse response,
                                                         @PathVariable Long challengeId, @PathVariable Long now) {
        if (challengeService.isOwnerOfChallenge(request.getRemoteUser(), challengeId)) {
            List<ChallengeRegistrantEntity> registrants = challengeService.findChallengeRegistrantWithinPeriod(challengeId, now, TimePeriodEnum.TWENTY_FOUR_HOURS);
            return registrants.stream()
                    .map(registrant -> registrant.getRegistrantFirstName() + " " + registrant.getRegistrantLastName())
                    .collect(toList());
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return null;
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "user/challenge/sendMailToDaily/{challengeId}/{now}", method = RequestMethod.POST)
    public void sendEmailToDailyChallengeRegistrants(HttpServletRequest request, HttpServletResponse response,
                                                     @PathVariable Long challengeId, @PathVariable Long now, @RequestBody EmailContent emailContent) {
        if (!challengeService.sendEmailToDailyChallengeRegistrants(request.getRemoteUser(), challengeId, now, emailContent)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "user/challenge/feedback/{registrantId}", method = RequestMethod.POST)
    public void sendFeedbackToRegistrant(HttpServletRequest request, HttpServletResponse response,
                                         @PathVariable Long registrantId, @RequestBody EmailContent emailContent) {
        if (!challengeService.sendEmailToRegistrant(request.getRemoteUser(), registrantId, emailContent)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "user/challenge/accept/{registrantId}/{phase}", method = RequestMethod.GET)
    public ChallengeRegistrantDto acceptChallengeRegistrant(HttpServletRequest request, @PathVariable Long registrantId, @PathVariable ChallengePhaseEnum phase) {
        return challengeService.acceptRegistrant(request.getRemoteUser(), registrantId, phase);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @RequestMapping(value = "/user/employer/saveEmailSetting", method = RequestMethod.POST)
    public EmailSettingDto saveEmployerEmailSetting(HttpServletRequest request, @RequestBody EmailSettingDto emailSettingDto) {
        emailSettingDto.setEmployerEmail(request.getRemoteUser());
        return employerService.saveEmployerEmailSetting(emailSettingDto);
    }

    @PreAuthorize("hasAnyAuthority('EMPLOYER')")
    @RequestMapping(value = "/user/employer/emailSetting", method = RequestMethod.GET)
    public EmailSettingDto findEmployerEmailSetting(HttpServletRequest request) {
        return employerService.findEmployerEmailSetting(request.getRemoteUser());
    }

    @RequestMapping(value = "/emailTemplates/{language}", method = RequestMethod.GET)
    public List<EmailTemplateDto> getAvailableEmailTemplates(@PathVariable Language language) {
        return emailService.getAvailableEmailTemplates(language);
    }

    @RequestMapping(value = "/emailTemplate/{templateId}", method = RequestMethod.GET)
    public EmailTemplateDto getTemplateById(@PathVariable Long templateId) {
        return emailService.getTemplateById(templateId);
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "user/challenge/qualifyAllRegistrants", method = RequestMethod.POST)
    public int qualifyAllRegistrants(@RequestBody ChallengeQualificationDto challengeQualificationDto,
                                     HttpServletRequest request, HttpServletResponse response) {
        String ownerEmail = request.getRemoteUser();
        if (challengeService.isOwnerOfChallenge(ownerEmail, challengeQualificationDto.getChallengeId())) {
            return challengeService.qualifyAllRegistrants(request.getRemoteUser(), challengeQualificationDto);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return 0;
        }

    }

}
