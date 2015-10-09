package com.techlooper.model;

public enum EmailSentResultEnum {

    OK(200),

    ERROR(500);

    private int value;

    private EmailSentResultEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
