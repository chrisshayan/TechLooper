package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 1/26/16.
 */
public class JobSeekerDashBoardCriteria {

    private String jobSeekerEmail;

    private JobSeekerPhaseEnum jobSeekerPhase;

    public String getJobSeekerEmail() {
        return jobSeekerEmail;
    }

    public void setJobSeekerEmail(String jobSeekerEmail) {
        this.jobSeekerEmail = jobSeekerEmail;
    }

    public JobSeekerPhaseEnum getJobSeekerPhase() {
        return jobSeekerPhase;
    }

    public void setJobSeekerPhase(JobSeekerPhaseEnum jobSeekerPhase) {
        this.jobSeekerPhase = jobSeekerPhase;
    }
}
