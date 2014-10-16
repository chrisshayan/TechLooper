package com.techlooper.vnw.integration;

import com.techlooper.enu.RouterConstant;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import net.minidev.json.JSONArray;
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

  @Value("${vnw.api.configuration.category.it.software.en}")
  private String category;

  public void process(Exchange exchange) throws Exception {
    Message message = exchange.getIn();
    message.setHeader(Exchange.HTTP_METHOD, "POST");
    message.setHeader(apiKeyName, apiKeyValue);
    JobSearchModel model = exchange.getProperty(RouterConstant.VNW_MODEL, JobSearchModel.class);
    message.setBody(json(model));
  }

  private String json(JobSearchModel model) throws IOException, TemplateException {
    Map<String, String> values = new HashMap<String, String>();

    values.put("jobTitle", model.getRequest().getTerms());
    values.put("pageNumber", model.getRequest().getPageNumber());

    JSONArray categoryIds = model.getConfiguration().read("$.categories[?(@.lang_en=='" + category + "')].category_id");
    values.put("category", (String) categoryIds.get(0));

    StringWriter jsonWriter = new StringWriter();
    freemarkerConfiguration.getTemplate("vnw/job-search-request.json.ftl").process(values, jsonWriter);
    return jsonWriter.toString();
  }
}
