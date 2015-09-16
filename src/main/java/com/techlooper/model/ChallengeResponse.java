package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 9/16/15.
 */
public class ChallengeResponse {

    private Long challengeId;

    private Integer leadAPIResponseCode;

    public ChallengeResponse() {
    }

    public ChallengeResponse(Long challengeId, Integer leadAPIResponseCode) {
        this.challengeId = challengeId;
        this.leadAPIResponseCode = leadAPIResponseCode;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
    }

    public Integer getLeadAPIResponseCode() {
        return leadAPIResponseCode;
    }

    public void setLeadAPIResponseCode(Integer leadAPIResponseCode) {
        this.leadAPIResponseCode = leadAPIResponseCode;
    }
}
