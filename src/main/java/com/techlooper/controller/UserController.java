package com.techlooper.controller;

import com.techlooper.entity.SalaryReview;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.*;
import com.techlooper.service.UserEvaluationService;
import com.techlooper.service.UserImportDataProcessor;
import com.techlooper.service.UserService;
import org.jasypt.util.text.TextEncryptor;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Base64;
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
    }
    else {
      httpServletResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
    }
  }

  @RequestMapping(value = "/user/save", method = RequestMethod.POST)
  public List<FieldError> save(@RequestBody @Valid UserInfo userInfo, BindingResult result, HttpServletResponse httpServletResponse) {
    if (result.getFieldErrorCount() > 0) {
      httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    else {
      userService.registerVietnamworksAccount(userInfo);
      userService.save(userInfo);
    }
    return result.getFieldErrors();
  }

  @RequestMapping(value = "/api/user/findTalent", method = RequestMethod.POST)
  public TalentSearchResponse findTalent(@RequestBody(required = false) TalentSearchRequest param, HttpServletResponse httpServletResponse) {
    return userService.findTalent(param);
  }

  @RequestMapping(value = "/api/user/talentProfile/{hashEmail}", method = RequestMethod.GET)
  public TalentProfile getTalentProfile(@PathVariable String hashEmail, HttpServletResponse httpServletResponse) {
    String email = new String(Base64.getDecoder().decode(hashEmail));
    return userService.getTalentProfile(email);
  }

  @RequestMapping(value = "/api/user/register", method = RequestMethod.POST)
  public List<FieldError> registerUser(@RequestBody @Valid UserInfo userInfo, BindingResult result, HttpServletResponse httpServletResponse) {
    if (result.getFieldErrorCount() > 0) {
      httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    else {
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
  public SalaryReport evaluateJobOffer(SalaryReview salaryReview) {
      return userEvaluationService.evaluateJobOffer(salaryReview);
  }
    
}
