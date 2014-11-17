package com.techlooper.model;

/**
 * Created by phuonghqh on 11/11/14.
 */
public enum HistogramEnum {

  THIRTY_DAYS(30, "d"),
  TWO_WEEKS(2, "w"),
  TWO_MONTHS(2, "M"),
  TWO_QUARTERS(6, "M");

  private Integer length;

  private String period;

  private HistogramEnum(Integer length, String period) {
    this.length = length;
    this.period = period;
  }

  public String toRange() {
    return length + period;
  }

  /**
   * @return the length of each {@link #getPeriod()}
   */
  public Integer getLength() {
    return length;
  }

  /**
   * @return d as a day, w as a week and M as a month.
   */
  public String getPeriod() {
    return period;
  }
}
