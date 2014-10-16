package com.techlooper.vnw.integration;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.techlooper.model.JobSearchRequest;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.camel.Exchange;
import org.apache.camel.component.http4.helper.HttpHelper;
import org.apache.camel.spi.DataFormat;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

/**
 * Created by phuonghqh on 10/16/14.
 */

@Component("vnwConfigurationDataFormat")
public class ConfigurationDataFormat implements DataFormat {

  @Value("${vnw.api.configuration.category.it.software.en}")
  private String category;

  public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
  }

  public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
    String json = IOUtils.toString(stream);
    ReadContext jsonPath = JsonPath.parse(json);
    if (!"200".equals(jsonPath.read("$.meta.code"))) {
      throw new Exception("Invalid Vietnamworks Configuration.");
    }
    JSONObject jsObject = new JSONObject();
    jsObject.put("locations", (JSONArray) jsonPath.read("$.data.locations[*]"));
    JSONArray categoryIds = jsonPath.read("$.data.categories[?(@.lang_en=='" + category + "')].category_id");
    jsObject.put("categoryId", categoryIds.get(0));
    return jsObject;
  }
}
