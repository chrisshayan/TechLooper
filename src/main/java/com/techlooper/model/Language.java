package com.techlooper.model;

/**
 * Created by phuonghqh on 5/29/15.
 */
public enum Language {
  vi("vi"), en("en");

  private final String value;

  private Language(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
