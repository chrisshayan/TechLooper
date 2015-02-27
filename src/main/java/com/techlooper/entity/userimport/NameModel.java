package com.techlooper.entity.userimport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by chris on 2/26/15.
 */
@JsonIgnoreProperties
public class NameModel {
    private String givenName, familyName, formatted;

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFormatted() {
        return formatted;
    }

    public void setFormatted(String formatted) {
        this.formatted = formatted;
    }
}
