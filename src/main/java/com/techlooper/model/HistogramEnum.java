package com.techlooper.model;

/**
 * Created by phuonghqh on 11/11/14.
 */
public enum HistogramEnum {

  DAY_PER_WEEK(7, 1, "d"),
  FIVE_DAYS_PER_MONTH(30, 5, "d"),
  FIFTEEN_DAYS_PER_QUARTER(90, 15, "d"),
  TWO_WEEKS(2, 1, "w"),
  TWO_MONTHS(2, 1, "M"),
  TWO_QUARTERS(2, 3, "M");

  private Integer total;

  private Integer period;

  private String unit;

  private HistogramEnum(Integer total, Integer period, String unit) {
    this.total = total;
    this.period = period;
    this.unit = unit;
  }

  /**
   * @return the total of each {@link #getUnit and #getPeriod}
   */
  public Integer getTotal() {
    return total;
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
