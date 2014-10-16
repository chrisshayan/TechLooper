package com.techlooper.vnw.integration;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.techlooper.enu.RouterConstant;
import com.techlooper.model.JobSearchRequest;
import net.minidev.json.JSONObject;
import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by phuonghqh on 10/16/14.
 */

@Component("vnwConfigurationDataFormat")
public class ConfigurationDataFormat implements DataFormat {

  public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
  }

  public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
    String json = IOUtils.toString(stream);
    ReadContext jsonPath = JsonPath.parse(json);
    if (!"200".equals(jsonPath.read("$.meta.code"))) {
      throw new Exception("Invalid Vietnamworks Configuration.");
    }

    JobSearchModel.Builder model = new JobSearchModel.Builder()
      .withConfiguration(JsonPath.parse(((JSONObject) jsonPath.read("$.data")).toJSONString()))
      .withRequest(exchange.getProperty(RouterConstant.VNW_MODEL, JobSearchRequest.class));
    exchange.setProperty(RouterConstant.VNW_MODEL, model.build());
    return null;
  }
}
