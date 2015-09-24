package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.ProjectRegistrantEntity;
import com.techlooper.entity.VnwUserProfile;
import com.techlooper.entity.WebinarEntity;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ProjectRegistrantRepository;
import com.techlooper.repository.elasticsearch.WebinarRepository;
import com.techlooper.service.VietnamWorksUserService;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.util.Iterator;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

/**
 * @author khoa-nd
 * @see com.techlooper.service.impl.VietnamWorksUserServiceImpl
 */
@Service
public class VietnamWorksUserServiceImpl implements VietnamWorksUserService {

  private static final String RESPONSE_CODE_SUCCESS = "200";

//  private static final String ACCOUNT_STATUS_NEW = "NEW";

  @Resource
  private RestTemplate restTemplate;

  @Resource
  private JobSearchAPIConfigurationRepository apiConfiguration;

//  public boolean existUser(String userEmail) {
//    HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(
//      MediaType.APPLICATION_JSON, apiConfiguration.getApiKeyName(),
//      apiConfiguration.getApiKeyValue(), EMPTY);
//    String requestUrl = apiConfiguration.getAccountStatus() + "/" + userEmail;
//    ResponseEntity<String> responseEntity = restTemplate.exchange(
//      requestUrl, HttpMethod.POST, requestEntity, String.class);
//    String responseBody = responseEntity.getBody();
//    return !responseBody.contains(ACCOUNT_STATUS_NEW);
//  }

  public boolean register(VnwUserProfile userProfile) {
    final String userInfoParameters = JsonUtils.toJSON(userProfile).orElse(EMPTY);
    HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(
      MediaType.APPLICATION_JSON, apiConfiguration.getApiKeyName(),
      apiConfiguration.getApiKeyValue(), userInfoParameters);
    ResponseEntity<String> responseEntity = restTemplate.exchange(
      apiConfiguration.getRegisterUrl(), HttpMethod.POST, requestEntity, String.class);
    String responseBody = responseEntity.getBody();
    return responseBody.contains(RESPONSE_CODE_SUCCESS);
  }
}