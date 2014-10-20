package com.techlooper.vnw.integration;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by phuonghqh on 10/16/14.
 */
public class ConfigurationModel implements Serializable {

  private ReadContext configuration;

  private void writeObject(ObjectOutputStream o) throws IOException {
    o.writeObject(configuration.json());
  }

  private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {
    configuration = JsonPath.parse(o.readObject());
  }

  public ReadContext getConfiguration() {
    return configuration;
  }

  public void setConfiguration(ReadContext configuration) {
    this.configuration = configuration;
  }

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
}
