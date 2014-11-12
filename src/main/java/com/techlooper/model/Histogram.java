package com.techlooper.model;

import java.util.List;

/**
 * Created by phuonghqh on 11/12/14.
 */
public class Histogram {

  private HistogramEnum name;

  private List<Long> values;

  public static class Builder {

    private Histogram instance = new Histogram();

    public Builder withValues(List<Long> values) {
      instance.values = values;
      return this;
    }

    public Builder withName(HistogramEnum name) {
      instance.name = name;
      return this;
    }

    public Histogram build() {
      return instance;
    }
  }

  public HistogramEnum getName() {
    return name;
  }

  public void setName(HistogramEnum name) {
    this.name = name;
  }

  public List<Long> getValues() {
    return values;
  }

  public void setValues(List<Long> values) {
    this.values = values;
  }
}
