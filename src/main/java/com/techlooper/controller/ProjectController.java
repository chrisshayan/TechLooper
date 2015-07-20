package com.techlooper.controller;

import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ProjectEntity;
import com.techlooper.model.ProjectDetailDto;
import com.techlooper.model.ProjectDto;
import com.techlooper.model.ProjectRegistrantDto;
import com.techlooper.service.ProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.GET)
    public ProjectDetailDto getProjectDetail(@PathVariable Long projectId) throws Exception {
        return projectService.getProjectDetail(projectId);
    }

    @RequestMapping(value = "/project/join", method = RequestMethod.POST)
    public long joinProject(@RequestBody ProjectRegistrantDto projectRegistrantDto) throws Exception {
        return projectService.joinProject(projectRegistrantDto);
    }

}
