package com.techlooper.controller;

import com.techlooper.entity.ProjectEntity;
import com.techlooper.model.ProjectDto;
import com.techlooper.service.ProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "/project/post", method = RequestMethod.POST)
    public long postProject(@RequestBody ProjectDto projectDto, HttpServletRequest servletRequest) throws Exception {
        projectDto.setAuthorEmail(servletRequest.getRemoteUser());
        ProjectEntity projectEntity = projectService.saveProject(projectDto);
        return projectEntity.getProjectId();
    }

    @RequestMapping(value = "/project/list", method = RequestMethod.GET)
    public List<ProjectDto> listProject() throws Exception {
        return projectService.listProject();
    }

}
