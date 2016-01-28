package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 1/26/16.
 */
public enum JobSeekerPhaseEnum {

    ALL("ALL"),
    ACTIVE("ACTIVE"),
    FINISHED("FINISHED"),
    DISQUALIFIED("DISQUALIFIED");

    private String value;

    JobSeekerPhaseEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
