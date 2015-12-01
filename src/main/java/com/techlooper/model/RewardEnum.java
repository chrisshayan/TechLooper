package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 9/22/15.
 */
public enum RewardEnum {

    FIRST_PLACE("1st place"),
    SECOND_PLACE("2nd place"),
    THIRD_PLACE("3rd place");

    private String value;

    RewardEnum(String value) {
        this.value = value;
    }

}
