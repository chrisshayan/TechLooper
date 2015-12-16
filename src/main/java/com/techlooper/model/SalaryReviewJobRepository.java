package com.techlooper.model;

import com.techlooper.entity.JobEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by NguyenDangKhoa on 12/7/15.
 */
public class SalaryReviewJobRepository {

    private static final int UPPER_LIMIT = 10;

    private Set<JobEntity> jobs = new HashSet<>();

    public boolean isEnough() {
        return jobs.size() > UPPER_LIMIT;
    }

    public boolean isNotEnough() {
        return !isEnough();
    }

    public boolean isEmpty() {
        return jobs.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public Set<JobEntity> getJobs() {
        return jobs;
    }

    public void addStrategy(JobSearchStrategy strategy) {
        jobs.addAll(strategy.searchJob());
    }

}
