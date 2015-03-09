package com.techlooper.model;

/**
 * Created by phuonghqh on 11/11/14.
 */
public enum HistogramEnum {

    ONE_WEEK(7, 1, "d"),
    SIX_BLOCKS_OF_FIVE_DAYS(6, 5, "d"),
    SIX_BLOCKS_OF_FIFTEEN_DAYS(6, 15, "d"),
    ONE_MONTH(30, 1, "d"),
    EIGHTEEN_BLOCKS_OF_FIVE_DAYS(18, 5, "d"),
    SIX_MONTHS(18, 10, "d"),
    ONE_YEAR(12, 31, "d"),
    TWO_WEEKS(2, 1, "w"),
    TWO_MONTHS(2, 1, "M"),
    TWO_QUARTERS(2, 3, "M"),
    TWO_SIX_MONTHS(2, 6, "M"),
    TWO_YEARS(2, 12, "M");

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
