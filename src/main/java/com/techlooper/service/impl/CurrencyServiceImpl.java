package com.techlooper.service.impl;

import com.techlooper.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Scanner;

/**
 * Created by phuonghqh on 5/28/15.
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {

  private final static Logger LOGGER = LoggerFactory.getLogger(CurrencyServiceImpl.class);

  @Resource
  private RestTemplate restTemplate;

  @Value("${api.exchangeRateUrl}")
  private String apiExchangeRateUrl;

  @Value("${usd2vnd}")
  private Long usd2vnd;

  public Long usdToVndRate() {
//    String url = String.format(apiExchangeRateUrl, "USD", "VND");
    Long result = usd2vnd;
//    try {
//      String rate = restTemplate.getForEntity(url, String.class).getBody();
//      Scanner scanner = new Scanner(rate);
//      while (!scanner.hasNextDouble()) {
//        scanner.next();
//      }
//      result = Double.valueOf(scanner.nextDouble()).longValue();
//    }
//    catch (Exception e) {
//      LOGGER.error("Failed load currency from api {}", url);
//    }
    return result;
  }
}