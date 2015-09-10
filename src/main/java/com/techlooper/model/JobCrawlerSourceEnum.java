package com.techlooper.model;

public enum JobCrawlerSourceEnum {

    VIETNAMWORKS("vietnamworks");

    private String value;

    private JobCrawlerSourceEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
