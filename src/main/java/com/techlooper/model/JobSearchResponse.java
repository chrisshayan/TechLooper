package com.techlooper.model;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by phuonghqh on 10/15/14.
 */
public class JobSearchResponse {

  private Integer total;

  private Set<JobResponse> jobs;

  public static class Builder {

    private JobSearchResponse instance = new JobSearchResponse();

    public Builder() {
      instance.jobs = new LinkedHashSet<JobResponse>();
    }

    public Builder withTotal(Integer total) {
      instance.total = total;
      return this;
    }

    public Builder withJob(JobResponse jobResponse) {
      instance.jobs.add(jobResponse);
      return this;
    }

    public JobSearchResponse build() {
      return instance;
    }
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public Set<JobResponse> getJobs() {
    return jobs;
  }

  public void setJobs(Set<JobResponse> jobs) {
    this.jobs = jobs;
  }
}
