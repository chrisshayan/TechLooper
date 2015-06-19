package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 6/19/15.
 */
public class GetPromotedSurveyRequest {

    private GetPromotedRequest getPromotedRequest;

    private GetPromotedSurvey getPromotedSurvey;

    public GetPromotedRequest getGetPromotedRequest() {
        return getPromotedRequest;
    }

    public void setGetPromotedRequest(GetPromotedRequest getPromotedRequest) {
        this.getPromotedRequest = getPromotedRequest;
    }

    public GetPromotedSurvey getGetPromotedSurvey() {
        return getPromotedSurvey;
    }

    public void setGetPromotedSurvey(GetPromotedSurvey getPromotedSurvey) {
        this.getPromotedSurvey = getPromotedSurvey;
    }
}
