package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 1/29/16.
 */
public class JobSeekerChallengeStats {

    private JobSeekerPhaseEnum phase;

    private Integer count;

    public JobSeekerChallengeStats(JobSeekerPhaseEnum phase, Integer count) {
        this.phase = phase;
        this.count = count;
    }

    public JobSeekerPhaseEnum getPhase() {
        return phase;
    }

    public void setPhase(JobSeekerPhaseEnum phase) {
        this.phase = phase;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
