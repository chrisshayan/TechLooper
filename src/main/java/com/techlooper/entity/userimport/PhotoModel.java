package com.techlooper.entity.userimport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by chris on 2/26/15.
 */
@JsonIgnoreProperties
public class PhotoModel {
    private String value, type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
