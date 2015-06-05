package com.techlooper.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.techlooper.service.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 5/28/15.
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {

  @Resource
  private RestTemplate restTemplate;

  @Value("${api.exchangeRateUrl}")
  private String apiExchangeRateUrl;

  @Value("${usd2vnd}")
  private Long usd2vnd;

  public Long usdToVndRate() {
//    String url = String.format(apiExchangeRateUrl, "USD_VND");
//    return restTemplate.getForEntity(url, JsonNode.class).getBody().findPath("val").asLong(0L);
    return usd2vnd;
  }
}
