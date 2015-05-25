package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 5/25/15.
 */
public class PriceJobSurvey {

    private Long priceJobId;

    private Boolean isUnderstandable;

    private Boolean isAccurate;

    private String feedback;

    public Long getPriceJobId() {
        return priceJobId;
    }

    public void setPriceJobId(Long priceJobId) {
        this.priceJobId = priceJobId;
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

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
