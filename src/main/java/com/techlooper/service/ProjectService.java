package com.techlooper.service;

import com.techlooper.entity.ProjectEntity;
import com.techlooper.model.ProjectDto;

/**
 * Created by NguyenDangKhoa on 7/10/15.
 */
public interface ProjectService {

    ProjectEntity saveProject(ProjectDto projectDto);

}
