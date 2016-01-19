package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 1/19/16.
 */
public class JobSeekerDashBoardInfo {

    private String email;

    private List<ChallengeDashBoardInfo> challenges;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ChallengeDashBoardInfo> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<ChallengeDashBoardInfo> challenges) {
        this.challenges = challenges;
    }
}
