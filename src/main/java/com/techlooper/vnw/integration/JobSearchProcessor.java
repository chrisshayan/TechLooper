package com.techlooper.vnw.integration;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by phuonghqh on 10/14/14.
 */

@Component("vnwJobSearchProcessor")
public class JobSearchProcessor implements Processor {

  @Resource
  private Configuration freemarkerConfiguration;

  @Value("${vnw.api.key.name}")
  private String apiKeyName;

  @Value("${vnw.api.key.value}")
  private String apiKeyValue;

  public void process(Exchange exchange) throws Exception {
    Message message = exchange.getIn();
    message.setHeader(Exchange.HTTP_METHOD, "POST");
    message.setHeader(apiKeyName, apiKeyValue);
    message.setBody(json(message.getBody(JobSearchModel.class)));
  }

  private String json(JobSearchModel model) throws IOException, TemplateException {
    Template template = freemarkerConfiguration.getTemplate("vnw/job-search-request.json.ftl");
    Map<String, String> valueMap = new HashMap<String, String>();
    valueMap.put("jobTitle", model.getRequest().getTerms());
    valueMap.put("pageNumber", model.getRequest().getPageNumber());
    valueMap.put("category", (String) model.getConfiguration().get("categoryId"));
    StringWriter jsonWriter = new StringWriter();
    template.process(valueMap, jsonWriter);
    return jsonWriter.toString();
  }
}
