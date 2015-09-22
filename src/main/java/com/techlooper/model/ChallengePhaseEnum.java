package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 9/22/15.
 */
public enum ChallengePhaseEnum {

    REGISTRATION("startDateTime", "registrationDateTime"),

    IN_PROGRESS("registrationDateTime", "submissionDateTime");

    private String fromDateTimeField;

    private String toDateTimeField;

    private ChallengePhaseEnum(String fromDateTimeField, String toDateTimeField) {
        this.fromDateTimeField = fromDateTimeField;
        this.toDateTimeField = toDateTimeField;
    }

    public String getFromDateTimeField() {
        return fromDateTimeField;
    }

    public void setFromDateTimeField(String fromDateTimeField) {
        this.fromDateTimeField = fromDateTimeField;
    }

    public String getToDateTimeField() {
        return toDateTimeField;
    }

    public void setToDateTimeField(String toDateTimeField) {
        this.toDateTimeField = toDateTimeField;
    }
}
