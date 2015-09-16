package com.techlooper.model;

/**
 * Created by NguyenDangKhoa on 9/16/15.
 */
public class ProjectResponse {

    private Long projectId;

    private Integer leadAPIResponseCode;

    public ProjectResponse() {
    }

    public ProjectResponse(Long projectId, Integer leadAPIResponseCode) {
        this.projectId = projectId;
        this.leadAPIResponseCode = leadAPIResponseCode;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getLeadAPIResponseCode() {
        return leadAPIResponseCode;
    }

    public void setLeadAPIResponseCode(Integer leadAPIResponseCode) {
        this.leadAPIResponseCode = leadAPIResponseCode;
    }
}
