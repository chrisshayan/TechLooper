package com.techlooper.vnw.integration;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.techlooper.model.JobResponse;
import com.techlooper.model.JobSearchResponse;
import freemarker.template.Configuration;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

/**
 * Created by phuonghqh on 10/15/14.
 */

@Component("vnwJobSearchDataFormat")
public class JobSearchDataFormat implements DataFormat {

  @Resource
  private Configuration freemarkerConfiguration;

  public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
  }

  public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
    String json = IOUtils.toString(stream);
    ReadContext jsonPath = JsonPath.parse(json);
    if (!"200".equals(jsonPath.read("$.meta.code"))) {
      throw new Exception("Invalid Vietnamworks Jobs.");
    }

    JobSearchResponse.Builder responseBuilder = new JobSearchResponse.Builder();
    responseBuilder.withTotal(JsonPath.read(json, "$.data.total"));

    String[] titles = ((List<String>)jsonPath.read("$.data.jobs[*].job_title")).toArray(new String[] {});
    String[] detailUrls = ((List<String>)jsonPath.read("$.data.jobs[*].job_detail_url")).toArray(new String[]{});

    for (int i = 0; i < detailUrls.length; i++) {
      responseBuilder.withJob(new JobResponse.Builder().withTitle(titles[i]).withDetailUrl(detailUrls[i]).build());
    }

    return responseBuilder.build();
  }
}
