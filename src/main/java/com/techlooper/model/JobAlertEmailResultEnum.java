package com.techlooper.model;

public enum JobAlertEmailResultEnum {

    EMAIL_SENT(200),

    JOB_NOT_FOUND(400),

    SERVER_ERROR(500);

    private int value;

    private JobAlertEmailResultEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
