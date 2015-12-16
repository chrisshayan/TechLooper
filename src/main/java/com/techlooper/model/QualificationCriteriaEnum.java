package com.techlooper.model;

public enum QualificationCriteriaEnum {

    HAVE_ALL_SUBMISSION_READ("HAVE_ALL_SUBMISSION_READ"),
    HAVE_SUBMISSION("HAVE_SUBMISSION"),
    REGARDLESS_OF_HAVING_SUBMISSION_OR_NOT("REGARDLESS_OF_HAVING_SUBMISSION_OR_NOT");

    private String value;

    QualificationCriteriaEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
