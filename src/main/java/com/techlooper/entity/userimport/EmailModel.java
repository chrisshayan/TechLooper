package com.techlooper.entity.userimport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by chris on 2/26/15.
 */
@JsonIgnoreProperties
public class EmailModel {
    private boolean primary;
    private String value;

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
