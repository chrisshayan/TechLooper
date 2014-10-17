package com.techlooper.vnw.integration;

import com.jayway.jsonpath.ReadContext;

import java.io.Serializable;

/**
 * Created by phuonghqh on 10/16/14.
 */
public class ConfigurationModel implements Serializable {

  private ReadContext configuration;

  public static class Builder {
    private ConfigurationModel instance = new ConfigurationModel();

    public Builder withConfiguration(ReadContext configuration) {
      instance.configuration = configuration;
      return this;
    }

    public ConfigurationModel build() {
      return instance;
    }
  }

  public ReadContext getConfiguration() {
    return configuration;
  }

  public void setConfiguration(ReadContext configuration) {
    this.configuration = configuration;
  }
}
