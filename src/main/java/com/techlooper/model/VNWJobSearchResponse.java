package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by NguyenDangKhoa on 10/25/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VNWJobSearchResponse {

    @JsonProperty(value = "data")
    private VNWJobSearchResponseData data;

    public VNWJobSearchResponseData getData() {
        return data;
    }

    public void setData(VNWJobSearchResponseData data) {
        this.data = data;
    }
}
