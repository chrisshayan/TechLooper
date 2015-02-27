package com.techlooper.entity.userimport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by chris on 2/26/15.
 */
@JsonIgnoreProperties
public class UrlModel {
    private String value, title;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
