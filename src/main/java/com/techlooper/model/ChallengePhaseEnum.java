package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 9/22/15.
 */
public enum ChallengePhaseEnum {

    ALL_PHASES("startDateTime", "registrationDateTime", "ALL_PHASE", 0, "", ""),
    REGISTRATION("startDateTime", "registrationDateTime", "REGISTRATION", 1, "Registration", "Đăng Ký"),
    IN_PROGRESS("registrationDateTime", "submissionDateTime", "IN_PROGRESS", 2, "", ""),
    IDEA("registrationDateTime", "ideaSubmissionDateTime", "IDEA", 3, "Idea", "Ý Tưởng"),
    UIUX("ideaSubmissionDateTime", "uxSubmissionDateTime", "UIUX", 4, "UI/UX", "UI/UX"),
    PROTOTYPE("uxSubmissionDateTime", "prototypeSubmissionDateTime", "PROTOTYPE", 5, "Prototype", "Prototype"),
    FINAL("prototypeSubmissionDateTime", "submissionDateTime", "FINAL", 6, "Final App", "Sản Phẩm Cuối"),
    WINNER("submissionDateTime", "", "WINNER", 7, "", "");

    private String fromDateTimeField;

    private String toDateTimeField;

    private String value;

    private Integer order;

    private String en;

    private String vi;

    public static ChallengePhaseEnum[] ALL_CHALLENGE_PHASES = {REGISTRATION, IDEA, UIUX, PROTOTYPE, FINAL};

    ChallengePhaseEnum(String fromDateTimeField, String toDateTimeField, String value, Integer order, String en, String vi) {
        this.fromDateTimeField = fromDateTimeField;
        this.toDateTimeField = toDateTimeField;
        this.value = value;
        this.order = order;
        this.en = en;
        this.vi = vi;
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

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }
}
