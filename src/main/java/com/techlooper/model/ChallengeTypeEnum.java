package com.techlooper.model;

public enum ChallengeTypeEnum {

    PUBLIC("PUBLIC"),
    INTERNAL("INTERNAL");

    private String value;

    ChallengeTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
