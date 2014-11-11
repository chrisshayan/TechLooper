package com.techlooper.model;

import org.elasticsearch.Build;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuonghqh on 11/11/14.
 */
public class CountBucket {

  private String name;

  private String type;

  private List<Long> values;

  public static class Builder {
    private CountBucket instance = new CountBucket();


    public Builder withType(String type) {
      instance.type = type;
      return this;
    }


    public Builder withValues(List<Long> values) {
      instance.values = values;
      return this;
    }


    public Builder withName(String name) {
      instance.name = name;
      return this;
    }

    public CountBucket build() {
      return instance;
    }
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CountBucket that = (CountBucket) o;

    if (name != null ? !name.equals(that.name) : that.name != null) return false;
    if (type != null ? !type.equals(that.type) : that.type != null) return false;

    return true;
  }

  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (type != null ? type.hashCode() : 0);
    return result;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<Long> getValues() {
    return values;
  }

  public void setValues(List<Long> values) {
    this.values = values;
  }
}
