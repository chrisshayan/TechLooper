package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 9/22/15.
 */
public enum ChallengePhaseEnum {

    ALL_PHASES("startDateTime", "registrationDateTime", "ALL_PHASE", 0),
    REGISTRATION("startDateTime", "registrationDateTime", "REGISTRATION", 1),
    IN_PROGRESS("registrationDateTime", "submissionDateTime", "IN_PROGRESS", 2),
    IDEA("registrationDateTime", "ideaSubmissionDateTime", "IDEA", 3),
    UIUX("ideaSubmissionDateTime", "uxSubmissionDateTime", "UIUX", 4),
    PROTOTYPE("uxSubmissionDateTime", "prototypeSubmissionDateTime", "PROTOTYPE", 5),
    FINAL("prototypeSubmissionDateTime", "submissionDateTime", "FINAL", 6),
    WINNER("prototypeSubmissionDateTime", "submissionDateTime", "WINNER", 7);

    private String fromDateTimeField;

    private String toDateTimeField;

    private String value;

    private Integer order;

    ChallengePhaseEnum(String fromDateTimeField, String toDateTimeField, String value, Integer order) {
        this.fromDateTimeField = fromDateTimeField;
        this.toDateTimeField = toDateTimeField;
        this.value = value;
        this.order = order;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
