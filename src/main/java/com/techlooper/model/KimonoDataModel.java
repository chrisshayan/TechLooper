package com.techlooper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by NguyenDangKhoa on 8/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KimonoDataModel {

    private String name;

    private String lastrunstatus;

    public KimonoDataModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastrunstatus() {
        return lastrunstatus;
    }

    public void setLastrunstatus(String lastrunstatus) {
        this.lastrunstatus = lastrunstatus;
    }
}
