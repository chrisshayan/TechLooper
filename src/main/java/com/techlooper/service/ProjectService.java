package com.techlooper.service;

import com.techlooper.entity.ProjectEntity;
import com.techlooper.model.ProjectDetailDto;
import com.techlooper.model.ProjectDto;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 7/10/15.
 */
public interface ProjectService {

    ProjectEntity saveProject(ProjectDto projectDto);

    List<ProjectDto> listProject();

    ProjectDetailDto getProjectDetail(Long projectId);
}
