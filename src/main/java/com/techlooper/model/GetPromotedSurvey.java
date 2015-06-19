package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 6/19/15.
 */
public class GetPromotedSurvey {

    private Long getPromotedId;

    private Boolean isUnderstandable;

    private Boolean isAccurate;

    private Boolean wantToLearnMore;

    private String feedback;

    public Long getGetPromotedId() {
        return getPromotedId;
    }

    public void setGetPromotedId(Long getPromotedId) {
        this.getPromotedId = getPromotedId;
    }

    public Boolean getIsUnderstandable() {
        return isUnderstandable;
    }

    public void setIsUnderstandable(Boolean isUnderstandable) {
        this.isUnderstandable = isUnderstandable;
    }

    public Boolean getIsAccurate() {
        return isAccurate;
    }

    public void setIsAccurate(Boolean isAccurate) {
        this.isAccurate = isAccurate;
    }

    public Boolean getWantToLearnMore() {
        return wantToLearnMore;
    }

    public void setWantToLearnMore(Boolean wantToLearnMore) {
        this.wantToLearnMore = wantToLearnMore;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
