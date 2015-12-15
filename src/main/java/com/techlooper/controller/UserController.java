package com.techlooper.controller;

import com.techlooper.dto.*;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.GetPromotedEntity;
import com.techlooper.entity.PriceJobEntity;
import com.techlooper.entity.vnw.dto.VnwUserDto;
import com.techlooper.model.*;
import com.techlooper.service.*;
import org.dozer.Mapper;
import org.jasypt.util.text.TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
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

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private TextEncryptor textEncryptor;

    @Resource
    private PromotionService promotionService;

    @Resource
    private Mapper dozerMapper;

    @Resource
    private JobPricingService jobPricingService;

    @Resource
    private SalaryReviewService salaryReviewService;

    @Resource
    private JobStatisticService jobStatisticService;

    @Resource
    private ChallengeService challengeService;

    @Resource
    private EmployerService employerService;

    @Resource
    private WebinarService webinarService;

    @Resource
    private ProjectService projectService;

    @Resource
    private JobAggregatorService jobAggregatorService;

    @Resource
    private ChallengeEmailService challengeEmailService;

    @Resource
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private EmailService emailService;

    @Resource
    private ForumService forumService;

    @RequestMapping(value = "/user/verifyUserLogin", method = RequestMethod.POST)
    public void verifyUserLogin(@RequestBody SocialRequest searchRequest, @CookieValue("techlooper.key") String techlooperKey, HttpServletResponse httpServletResponse) {
        if (!textEncryptor.encrypt(searchRequest.getEmailAddress()).equals(techlooperKey)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @RequestMapping(value = "/priceJob", method = RequestMethod.POST)
    public PriceJobEntity priceJob(@RequestBody PriceJobEntity priceJobEntity) {
        jobPricingService.priceJob(priceJobEntity);
        return priceJobEntity;
    }

    @RequestMapping(value = "/savePriceJobSurvey", method = RequestMethod.POST)
    public void savePriceJobSurvey(@RequestBody PriceJobSurvey priceJobSurvey, HttpServletResponse httpServletResponse) {
        boolean isSaved = jobPricingService.savePriceJobSurvey(priceJobSurvey);
        if (isSaved) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @RequestMapping("promotion/citibank/creditCard")
    public void placeCitibankCreditCardPromotion(@Valid @RequestBody CitibankCreditCardPromotion citibankCreditCardPromotion) {
        promotionService.placePromotion(citibankCreditCardPromotion);
    }

    @RequestMapping(value = "/promotion/citibank/title/{lang}", method = RequestMethod.GET)
    public String getCitiBankPromotionTitle(@PathVariable String lang) {
        return emailService.getCitiBankPromotionTitle(lang);
    }

    @RequestMapping(value = "/getPromoted/email", method = RequestMethod.POST)
    public long sendTopDemandedSkillsEmail(@Valid @RequestBody GetPromotedEmailRequest emailRequest) {
        long getPromotedId = jobPricingService.saveGetPromotedInformation(emailRequest);

        if (getPromotedId != -1L && emailRequest.getHasResult()) {
            jobPricingService.sendTopDemandedSkillsEmail(getPromotedId, emailRequest);
        }

        return getPromotedId;
    }

    @RequestMapping(value = "/getPromoted/survey", method = RequestMethod.POST)
    public long saveGetPromotedSurvey(@RequestBody GetPromotedSurveyRequest getPromotedSurveyRequest, HttpServletResponse httpServletResponse) {
        return jobPricingService.saveGetPromotedSurvey(getPromotedSurveyRequest);
    }

    @RequestMapping(value = "/getPromotedResult/{id}", method = RequestMethod.GET)
    public GetPromotedEntity getPromotedResult(@PathVariable Long id) {
        return jobPricingService.getPromotedEntity(id);
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
            latestChallenge.setNumberOfRegistrants(challengeRegistrantService.getNumberOfRegistrants(latestChallenge.getChallengeId()));
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

        try {
            TopicList topicList = forumService.getLatestTopics();
            personalHomepage.setLatestTopics(topicList.getTopics().stream().limit(5).collect(toList()));
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        return personalHomepage;
    }

    @PreAuthorize("hasAnyAuthority('JOB_SEEKER', 'EMPLOYER')")
    @RequestMapping(value = "/user/current", method = RequestMethod.GET)
    public UserProfileDto getUserProfile(HttpServletRequest request) {
        LOGGER.debug("Reading current user profile info");
        org.springframework.security.core.Authentication authentication = (org.springframework.security.core.Authentication) request.getUserPrincipal();
        Object principal = authentication.getPrincipal();
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
        org.springframework.security.core.Authentication authentication = (org.springframework.security.core.Authentication) request.getUserPrincipal();
        Object principal = authentication.getPrincipal();
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
        if (challengeService.isChallengeOwner(request.getRemoteUser(), challengeId)) {
            List<ChallengeRegistrantEntity> registrants = challengeRegistrantService.findChallengeRegistrantWithinPeriod(challengeId, now, TimePeriodEnum.TWENTY_FOUR_HOURS);
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
        if (!challengeEmailService.sendEmailToDailyChallengeRegistrants(request.getRemoteUser(), challengeId, now, emailContent)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "user/challenge/feedback/{registrantId}", method = RequestMethod.POST)
    public void sendFeedbackToRegistrant(HttpServletRequest request, HttpServletResponse response,
                                         @PathVariable Long registrantId, @RequestBody EmailContent emailContent) {
        ChallengeRegistrantEntity registrant = challengeRegistrantService.findRegistrantById(registrantId);
        if (registrant != null) {
            String ownerEmail = request.getRemoteUser();
            Long challengeId = registrant.getChallengeId();
            if (challengeService.isChallengeOwner(ownerEmail, challengeId)) {
                boolean success = challengeEmailService.sendEmailToRegistrant(request.getRemoteUser(), registrantId, emailContent);
                if (!success) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "user/challenge/accept/{registrantId}/{phase}", method = RequestMethod.GET)
    public ChallengeRegistrantDto acceptChallengeRegistrant(HttpServletRequest request, HttpServletResponse response,
                                                            @PathVariable Long registrantId, @PathVariable ChallengePhaseEnum phase) {
        String ownerEmail = request.getRemoteUser();
        ChallengeRegistrantDto challengeRegistrantDto = new ChallengeRegistrantDto();
        ChallengeRegistrantEntity registrant = challengeRegistrantService.findRegistrantById(registrantId);
        if (registrant != null) {
            if (challengeService.isChallengeOwner(ownerEmail, registrant.getChallengeId())) {
                challengeRegistrantDto = challengeRegistrantService.acceptRegistrant(registrantId, phase);
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return challengeRegistrantDto;
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "user/challenge/reject", method = RequestMethod.POST)
    public ChallengeRegistrantDto rejectChallengeRegistrant(HttpServletRequest request, HttpServletResponse response, @RequestBody RejectRegistrantDto rejectRegistrantDto) {
        ChallengeRegistrantDto registrantDto = challengeRegistrantService.rejectRegistrant(request.getRemoteUser(), rejectRegistrantDto);
        if (registrantDto == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return registrantDto;
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

    @RequestMapping(value = "/emailTemplate/{templateId}/{challengeId}", method = RequestMethod.GET)
    public EmailTemplateDto getTemplateById(@PathVariable Long templateId, @PathVariable Long challengeId) {
        return emailService.getTemplateById(templateId, challengeId);
    }

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "user/challenge/qualifyAllRegistrants", method = RequestMethod.POST)
    public List<ChallengeRegistrantDto> qualifyAllRegistrants(@RequestBody ChallengeQualificationDto challengeQualificationDto,
                                                              HttpServletRequest request, HttpServletResponse response) {
        String ownerEmail = request.getRemoteUser();
        List<ChallengeRegistrantDto> qualifiedRegistrants = new ArrayList<>();
        if (challengeService.isChallengeOwner(ownerEmail, challengeQualificationDto.getChallengeId())) {
            qualifiedRegistrants = challengeRegistrantService.qualifyAllRegistrants(ownerEmail, challengeQualificationDto);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
        return qualifiedRegistrants;
    }

}
