package com.techlooper.model;

import com.techlooper.util.LookUpUtils;

/**
 * Created by NguyenDangKhoa on 11/5/14.
 */
public enum PeriodEnum {

    /**
     * Date Range : Last 7 days
     */
    WEEK("week"),

    /**
     * Date Range : Last 30 days
     */
    MONTH("month"),

    /**
     * Date Range : Last 90 days
     */
    QUARTER("quarter"),

    /**
     * Default is empty value
     */
    EMPTY("");

    private String value;

    PeriodEnum(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String toString() {
        return value;
    }

    public static PeriodEnum lookUp(String value) {
        return LookUpUtils.lookup(PeriodEnum.class, value, PeriodEnum.EMPTY);
    }
}
