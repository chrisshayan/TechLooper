package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VNWJobSearchResponseData {

    @JsonProperty(value = "total")
    private Integer total;

    @JsonProperty(value = "jobs")
    private Set<VNWJobSearchResponseDataItem> jobs;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Set<VNWJobSearchResponseDataItem> getJobs() {
        return jobs;
    }

    public void setJobs(Set<VNWJobSearchResponseDataItem> jobs) {
        this.jobs = jobs;
    }
}
