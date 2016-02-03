package com.techlooper.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuonghqh on 10/15/14.
 */
public class JobSearchResponse {

    private Long totalJob;

    private Integer totalPage;

    private Integer page;

    private List<JobResponse> jobs;

    public static class Builder {

        private JobSearchResponse instance = new JobSearchResponse();

        public Builder() {
            instance.jobs = new ArrayList<>();
        }

        public Builder withTotalJob(Long totalJob) {
            instance.totalJob = totalJob;
            return this;
        }

        public Builder withTotalPage(Integer totalPage) {
            instance.totalPage = totalPage;
            return this;
        }

        public Builder withPage(Integer page) {
            instance.page = page;
            return this;
        }

        public Builder withJobs(List<JobResponse> jobs) {
            instance.jobs = jobs;
            return this;
        }

        public JobSearchResponse build() {
            return instance;
        }
    }

    public Long getTotalJob() {
        return totalJob;
    }

    public void setTotalJob(Long totalJob) {
        this.totalJob = totalJob;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<JobResponse> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobResponse> jobs) {
        this.jobs = jobs;
    }

    public void addJobs(List<JobResponse> jobs) {
        if (this.jobs == null) {
            this.jobs = new ArrayList<>();
        }
        this.jobs.addAll(jobs);
    }
}
