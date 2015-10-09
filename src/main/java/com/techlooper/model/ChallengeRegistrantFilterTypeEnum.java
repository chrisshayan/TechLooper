package com.techlooper.model;

public enum ChallengeRegistrantFilterTypeEnum {

    BY_REGISTRANT("registrantId"),

    BY_SUBMISSION("challengeSubmission");

    private String value;

    private ChallengeRegistrantFilterTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
