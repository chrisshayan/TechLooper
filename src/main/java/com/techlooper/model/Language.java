package com.techlooper.model;

/**
 * Created by phuonghqh on 5/29/15.
 */
public enum Language {
    vi("VI"), en("EN");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Language parse(String value) {
        if ("vi".equalsIgnoreCase(value) || "vn".equalsIgnoreCase(value)) return vi;
        return en;
    }
}
