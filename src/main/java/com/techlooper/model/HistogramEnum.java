package com.techlooper.model;

/**
 * Created by phuonghqh on 11/11/14.
 */
public enum HistogramEnum {

  THIRTY_DAY(30, "d"),
  TWO_WEEK(2, "w"),
  TWO_MONTH(2, "M");

  private Integer length;

  private String period;

  private HistogramEnum(Integer length, String period) {
    this.length = length;
    this.period = period;
  }

  public Integer getLength() {
    return length;
  }

  public String getPeriod() {
    return period;
  }
}
