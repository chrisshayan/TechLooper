package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 9/15/15.
 */
public enum LeadEventEnum {

    POST_CHALLENGE(1),

    POST_FREELANCE_PROJECT(2);

    private int value;

    private LeadEventEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
