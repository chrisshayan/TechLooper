package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 3/28/15.
 */
public enum UpdateModeEnum {

    ADD_NEW("addNew"),
    OVERWRITE("overwrite"),
    MERGE("merge");

    private String value;

    public String getValue() {
        return value;
    }

    private UpdateModeEnum(String value) {
        this.value = value;
    }
}
