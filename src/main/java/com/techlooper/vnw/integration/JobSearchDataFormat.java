package com.techlooper.vnw.integration;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.techlooper.enu.RouterConstant;
import com.techlooper.model.JobResponse;
import com.techlooper.model.JobSearchResponse;
import net.minidev.json.JSONArray;
import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by phuonghqh on 10/15/14.
 */

@Component("vnwJobSearchDataFormat")
public class JobSearchDataFormat implements DataFormat {

  private static final Logger LOGGER = LoggerFactory.getLogger(JobSearchDataFormat.class);

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

    // TODO: find new way to make code shorten
    String[] urls = ((List<String>) jsonPath.read("$.data.jobs[*].job_detail_url")).toArray(new String[]{});
    String[] titles = ((List<String>) jsonPath.read("$.data.jobs[*].job_title")).toArray(new String[]{});
    String[] locations = ((List<String>) jsonPath.read("$.data.jobs[*].job_location")).toArray(new String[]{});
    String[] levels = ((List<String>) jsonPath.read("$.data.jobs[*].top_level")).toArray(new String[]{});
    String[] postedOns = ((List<String>) jsonPath.read("$.data.jobs[*].posted_date")).toArray(new String[]{});
    String[] companies = ((List<String>) jsonPath.read("$.data.jobs[*].job_company")).toArray(new String[]{});
    String[] videoUrls = ((List<String>) jsonPath.read("$.data.jobs[*].job_video_url")).toArray(new String[]{});
    String[] logoUrls = ((List<String>) jsonPath.read("$.data.jobs[*].job_logo_url")).toArray(new String[]{});

    ReadContext configuration = exchange.getProperty(RouterConstant.VNW_MODEL, JobSearchModel.class).getConfiguration();
    translateIds(locations, configuration, "$.locations[?(@.location_id=='%s')].lang_en");
    translateIds(levels, configuration, "$.degree[?(@.degree_id=='%s')].lang_en");

    for (int i = 0; i < urls.length; i++) {
      responseBuilder.withJob(new JobResponse.Builder()
        .withUrl(urls[i])
        .withTitle(titles[i])
        .withLocation(locations[i])
        .withLevel(levels[i])
        .withPostedOn(postedOns[i])
        .withCompany(companies[i])
        .withVideoUrl(videoUrls[i])
        .withLogoUrl(logoUrls[i])
        .build());
    }

    return responseBuilder.build();
  }

  private void translateIds(String[] values, ReadContext configuration, String jsonPathTemplate) {
    for (int i = 0; i < values.length; i++) {
      String value = values[i];
      String[] subs = value.split(",");
      for (String sub : subs) {
        JSONArray jsonArray = configuration.read(String.format(jsonPathTemplate, sub));
        if (jsonArray.size() == 0) {
          LOGGER.debug("Not found key [{}] from Configuration, jsonpath = {}", sub, String.format(jsonPathTemplate, sub));
          continue;
        }
        String lang = (String)(jsonArray).get(0);
        value = value.replaceAll(sub, lang);
      }
      values[i] = value;
    }
  }
}
