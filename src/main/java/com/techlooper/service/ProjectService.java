package com.techlooper.service;

import com.techlooper.entity.ProjectEntity;
import com.techlooper.entity.ProjectRegistrantEntity;
import com.techlooper.model.ProjectDetailDto;
import com.techlooper.model.ProjectDto;
import com.techlooper.model.ProjectRegistrantDto;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 7/10/15.
 */
public interface ProjectService {

    ProjectEntity saveProject(ProjectDto projectDto);

    List<ProjectDto> listProject();

    ProjectDetailDto getProjectDetail(Long projectId);

    void sendEmailAlertJobSeekerApplyJob(ProjectEntity projectEntity, ProjectRegistrantEntity projectRegistrantEntity)
            throws MessagingException, IOException, TemplateException;

    void sendEmailAlertEmployerApplyJob(ProjectEntity projectEntity, ProjectRegistrantEntity projectRegistrantEntity)
            throws MessagingException, IOException, TemplateException;

    long joinProject(ProjectRegistrantDto projectRegistrantDto) throws MessagingException, IOException, TemplateException;

    Long getNumberOfRegistrants(Long projectId);
}
