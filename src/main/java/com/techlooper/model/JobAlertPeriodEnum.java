package com.techlooper.model;

public enum JobAlertPeriodEnum {

    DAILY(1),

    WEEKLY(7),

    MONTHLY(30);

    private int value;

    private JobAlertPeriodEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
