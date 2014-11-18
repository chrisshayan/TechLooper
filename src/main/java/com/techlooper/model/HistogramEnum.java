package com.techlooper.model;

/**
 * Created by phuonghqh on 11/11/14.
 */
public enum HistogramEnum {

  THIRTY_DAYS(30, 1, "d"),
  LINE_CHART(12, 5, "d"),
  TWO_WEEKS(2, 1, "w"),
  TWO_MONTHS(2, 1, "M"),
  TWO_QUARTERS(2, 3, "M");

  private Integer range;

  private Integer period;

  private String unit;

  private HistogramEnum(Integer length, Integer period, String unit) {
    this.range = length;
    this.period = period;
    this.unit = unit;
  }

  /**
   * @return the range of each {@link #getUnit and #getPeriod}
   */
  public Integer getRange() {
    return range;
  }

  /**
   * @return period
   */
  public Integer getPeriod() {
    return period;
  }

  /**
   * @return d as a day, w as a week and M as a month.
   */
  public String getUnit() {
    return unit;
  }
}
