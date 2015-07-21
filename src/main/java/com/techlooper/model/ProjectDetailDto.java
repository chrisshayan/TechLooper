package com.techlooper.model;

import com.techlooper.entity.Company;

/**
 * Created by NguyenDangKhoa on 7/17/15.
 */
public class ProjectDetailDto {

    private ProjectDto project;

    private EmployerDto company;

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    public EmployerDto getCompany() {
        return company;
    }

    public void setCompany(EmployerDto company) {
        this.company = company;
    }
}
