package com.techlooper.model;

public enum JobTypeEnum {

    TOP_PRIORITY(1),

    NORMAL(2),

    ALL(0);

    private int value;

    private JobTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
