package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VNWJobSearchResponseData {

    @JsonProperty(value = "total")
    private Integer total;

    @JsonProperty(value = "jobs")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<VNWJobSearchResponseDataItem> jobs;

    private static VNWJobSearchResponseData defaultObject;

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

    public static VNWJobSearchResponseData getDefaultObject() {
        return Optional.ofNullable(defaultObject).orElseGet(() -> {
            defaultObject = new VNWJobSearchResponseData();
            defaultObject.setTotal(0);
            defaultObject.setJobs(Collections.emptySet());
            return defaultObject;
        });
    }
}
