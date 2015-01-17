package com.techlooper.service.impl;

import com.techlooper.model.VNWUserInfo;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.service.VietnamWorksUserService;
import com.techlooper.util.JsonUtils;
import com.techlooper.util.RestTemplateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * @author khoa-nd
 * @see com.techlooper.service.impl.VietnamworksUserService
 */
@Service
public class VietnamworksUserService {

  private static final String RESPONSE_CODE_SUCCESS = "200";

  private static final String ACCOUNT_STATUS_NEW = "NEW";

  @Resource
  private RestTemplate restTemplate;

  @Resource
  private JobSearchAPIConfigurationRepository apiConfiguration;

  public boolean existUser(String userEmail) {
    HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(
      MediaType.APPLICATION_JSON, apiConfiguration.getApiKeyName(),
      apiConfiguration.getApiKeyValue(), EMPTY);
    String requestUrl = apiConfiguration.getAccountStatus() + "/" + userEmail;
    ResponseEntity<String> responseEntity = restTemplate.exchange(
      requestUrl, HttpMethod.POST, requestEntity, String.class);
    String responseBody = responseEntity.getBody();
    return !responseBody.contains(ACCOUNT_STATUS_NEW);
  }

  public boolean register(VNWUserInfo userInfo) {
    final String userInfoParameters = JsonUtils.toJSON(userInfo).orElse(EMPTY);
    HttpEntity<String> requestEntity = RestTemplateUtils.configureHttpRequestEntity(
      MediaType.APPLICATION_JSON, apiConfiguration.getApiKeyName(),
      apiConfiguration.getApiKeyValue(), userInfoParameters);
    ResponseEntity<String> responseEntity = restTemplate.exchange(
      apiConfiguration.getRegisterUrl(), HttpMethod.POST, requestEntity, String.class);
    String responseBody = responseEntity.getBody();
    return responseBody.contains(RESPONSE_CODE_SUCCESS);
  }
}