package com.techlooper.vnw.integration;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by phuonghqh on 10/16/14.
 */
@Component("vnwConfigurationProcessor")
public class ConfigurationProcessor implements Processor {

  @Value("${vnw.api.key.name}")
  private String apiKeyName;

  @Value("${vnw.api.key.value}")
  private String apiKeyValue;

  public void process(Exchange exchange) throws Exception {
    Message message = exchange.getIn();
    message.setHeader(Exchange.HTTP_METHOD, "GET");
    message.setHeader(apiKeyName, apiKeyValue);
    message.setBody(null);
  }
}
