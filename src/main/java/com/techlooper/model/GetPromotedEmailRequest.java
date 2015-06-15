package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;

/**
 * Created by NguyenDangKhoa on 6/15/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPromotedEmailRequest {

    private Language lang;

    @Email
    private String email;

    private Boolean hasResult;

    private GetPromotedRequest getPromotedRequest;

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GetPromotedRequest getGetPromotedRequest() {
        return getPromotedRequest;
    }

    public void setGetPromotedRequest(GetPromotedRequest getPromotedRequest) {
        this.getPromotedRequest = getPromotedRequest;
    }

    public Boolean getHasResult() {
        return hasResult;
    }

    public void setHasResult(Boolean hasResult) {
        this.hasResult = hasResult;
    }
}
