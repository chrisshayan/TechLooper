package com.techlooper.model.vnw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VNWConfigurationResponse {

    @JsonProperty(value = "data")
    private VNWConfigurationResponseData data;

    public VNWConfigurationResponseData getData() {
        return data;
    }

    public void setData(VNWConfigurationResponseData data) {
        this.data = data;
    }
}
