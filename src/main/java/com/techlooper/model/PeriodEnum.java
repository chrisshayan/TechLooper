package com.techlooper.model;

import com.techlooper.util.LookUpUtils;

/**
 * Created by NguyenDangKhoa on 11/5/14.
 */
public enum PeriodEnum {

    /**
     * Date Range : Last 7 days
     */
    WEEK("week", 7),

    /**
     * Date Range : Last 30 days
     */
    MONTH("month", 30),

    /**
     * Date Range : Last 90 days
     */
    QUARTER("quarter", 90),

    /**
     * Default is empty value
     */
    EMPTY("", 0);

    private String value;
    private long numberOfDays;

    PeriodEnum(String value, long numberOfDays) {
        this.value = value;
        this.numberOfDays = numberOfDays;
    }

    public String value() {
        return value;
    }

    public long numberOfDays() {
        return numberOfDays;
    }

    public String toString() {
        return value;
    }

    public static PeriodEnum lookUp(String value) {
        return LookUpUtils.lookup(PeriodEnum.class, value, PeriodEnum.EMPTY);
    }
}
