package com.techlooper.controller;

import com.techlooper.entity.GetPromotedEntity;
import com.techlooper.entity.PriceJobEntity;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.*;
import com.techlooper.service.*;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.dozer.Mapper;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by phuonghqh on 12/23/14.
 */
@RestController
public class UserController {

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

    @Value("${mail.citibank.title.vn}")
    private String citibankTitleVn;

    @Value("${mail.citibank.title.en}")
    private String citibankTitleEn;

    @Value("classpath:braille.txt")
    private org.springframework.core.io.Resource brailleTextFile;

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

    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    public List<FieldError> save(@RequestBody @Valid UserInfo userInfo, BindingResult result, HttpServletResponse httpServletResponse) {
        if (result.getFieldErrorCount() > 0) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            userService.registerVietnamworksAccount(userInfo);
            userService.save(userInfo);
        }
        return result.getFieldErrors();
    }

    @RequestMapping(value = "/api/user/findTalent", method = RequestMethod.POST)
    public TalentSearchResponse findTalent(@RequestBody(required = false) TalentSearchRequest param, HttpServletResponse httpServletResponse) {
        return userService.findTalent(param);
    }

    @RequestMapping(value = "/api/user/register", method = RequestMethod.POST)
    public List<FieldError> registerUser(@RequestBody @Valid UserInfo userInfo, BindingResult result, HttpServletResponse httpServletResponse) {
        if (result.getFieldErrorCount() > 0) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            userService.registerUser(userInfo);
        }
        return result.getFieldErrors();
    }

    @SendToUser("/queue/info")
    @MessageMapping("/user/findByKey")
    @RequestMapping(value = "/user/findByKey", method = RequestMethod.POST)
    public UserInfo getUserInfo(@CookieValue("techlooper.key") String techlooperKey/*, @DestinationVariable String username */) {
        UserInfo userInfo = userService.findUserInfoByKey(techlooperKey);
        userInfo.getLoginSource();
        return userInfo;
    }

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
}
