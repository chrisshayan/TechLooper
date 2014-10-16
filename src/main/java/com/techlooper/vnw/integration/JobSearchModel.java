package com.techlooper.vnw.integration;

import com.techlooper.model.JobSearchRequest;
import net.minidev.json.JSONObject;

/**
 * Created by phuonghqh on 10/16/14.
 */
public class JobSearchModel {

  private JobSearchRequest request;

  private JSONObject configuration;

  public static class Builder {
    private JobSearchModel instance = new JobSearchModel();

    public Builder withConfiguration(JSONObject configuration) {
      instance.configuration = configuration;
      return this;
    }

    public Builder withRequest(JobSearchRequest request) {
      instance.request = request;
      return this;
    }

    public JobSearchModel build() {
      return instance;
    }
  }

  public JSONObject getConfiguration() {
    return configuration;
  }

  public void setConfiguration(JSONObject configuration) {
    this.configuration = configuration;
  }

  public JobSearchRequest getRequest() {
    return request;
  }

  public void setRequest(JobSearchRequest request) {
    this.request = request;
  }
}
