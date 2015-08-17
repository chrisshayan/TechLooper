package com.techlooper.model;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 8/17/15.
 */
public class JobListingModel {

    private Integer page;

    private Long totalPage;

    private Long totalJob;

    private List<JobResponse> jobs;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotalJob() {
        return totalJob;
    }

    public void setTotalJob(Long totalJob) {
        this.totalJob = totalJob;
    }

    public List<JobResponse> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobResponse> jobs) {
        this.jobs = jobs;
    }
}
