package com.techlooper.service;

import com.techlooper.entity.ProjectEntity;
import com.techlooper.entity.ProjectRegistrantEntity;
import com.techlooper.model.ProjectDetailDto;
import com.techlooper.model.ProjectDto;
import com.techlooper.model.ProjectRegistrantDto;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 7/10/15.
 */
public interface ProjectService {

    ProjectEntity saveProject(ProjectDto projectDto);

    List<ProjectDto> listProject();

    ProjectDetailDto getProjectDetail(Long projectId);

    void sendEmailAlertJobSeekerApplyJob(ProjectEntity projectEntity, ProjectRegistrantEntity projectRegistrantEntity);

    void sendEmailAlertEmployerApplyJob(ProjectEntity projectEntity, ProjectRegistrantEntity projectRegistrantEntity);

    long joinProject(ProjectRegistrantDto projectRegistrantDto);

    Long getNumberOfRegistrants(Long projectId);

    void sendEmailAlertEmployerPostJob(ProjectEntity projectEntity);

    void sendEmailAlertTechloopiesPostJob(ProjectEntity projectEntity);

    Long countTotalNumberOfProjects();

    Long countTotalNumberOfSkills();

    List<ProjectDto> findProjectByOwner(String ownerEmail);

    Long countRegistrantsByProjectId(Long projectId);
}
