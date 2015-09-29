package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 9/28/15.
 */
public enum TimePeriodEnum {

    TWENTY_FOUR_HOURS(86400000L);

    private Long miliseconds;

    public Long getMiliseconds() {
        return miliseconds;
    }

    private TimePeriodEnum(Long miliseconds) {
        this.miliseconds = miliseconds;
    }

}
