package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by NguyenDangKhoa on 10/25/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VNWJobSearchResponse {

    @JsonProperty(value = "data")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private VNWJobSearchResponseData data;

    private static VNWJobSearchResponse defaultObject;

    public VNWJobSearchResponseData getData() {
        return data;
    }

    public void setData(VNWJobSearchResponseData data) {
        this.data = data;
    }

    public static VNWJobSearchResponse getDefaultObject() {
        if (defaultObject != null) {
            return defaultObject;
        }
        defaultObject = new VNWJobSearchResponse();
        defaultObject.setData(VNWJobSearchResponseData.getDefaultObject());
        return defaultObject;
    }

    public boolean hasData() {
        return data.getJobs() != null && data.getJobs().size() > 0;
    }
}
