package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 1/26/16.
 */
public enum JobSeekerPhaseEnum {

    ALL("ALL", 1),
    ACTIVE("ACTIVE", 2),
    FINISHED("FINISHED", 3),
    DISQUALIFIED("DISQUALIFIED", 4);

    private String value;

    private int order;

    JobSeekerPhaseEnum(String value, int order) {
        this.value = value;
        this.order = order;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
