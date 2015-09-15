package com.techlooper.controller;

import com.techlooper.entity.ProjectEntity;
import com.techlooper.entity.vnw.VnwCompany;
import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.model.*;
import com.techlooper.service.EmployerService;
import com.techlooper.service.LeadAPIService;
import com.techlooper.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ProjectController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    @Resource
    private ProjectService projectService;

    @Resource
    private EmployerService employerService;

    @Resource
    private LeadAPIService leadAPIService;

    @PreAuthorize("hasAuthority('EMPLOYER')")
    @RequestMapping(value = "/project/post", method = RequestMethod.POST)
    public long postProject(@RequestBody ProjectDto projectDto, HttpServletRequest servletRequest) throws Exception {
        String employerEmail = servletRequest.getRemoteUser();
        projectDto.setAuthorEmail(employerEmail);
        ProjectEntity projectEntity = projectService.saveProject(projectDto);

        // Call Lead Management API to create new lead on CRM system
        try {
            VnwUser employer = employerService.findEmployerByUsername(employerEmail);
            VnwCompany company = employerService.findCompanyById(employer.getCompanyId());
            if (employer != null && company != null) {
                int responseCode = leadAPIService.createNewLead(employer, company, LeadEventEnum.POST_FREELANCE_PROJECT);

                String logMessage = "Create Lead API Response Code : %d ,EmployerID : %d ,CompanyID : %d";
                LOGGER.info(String.format(logMessage, responseCode, employer.getUserId(), company.getCompanyId()));
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

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

    @RequestMapping(value = "/project/stats", method = RequestMethod.GET)
    public ProjectStatsDto getProjectStatistic() throws Exception {
        ProjectStatsDto projectStatsDto = new ProjectStatsDto();
        projectStatsDto.setNumberOfProjects(projectService.countTotalNumberOfProjects());
        projectStatsDto.setNumberOfSkills(projectService.countTotalNumberOfSkills());
        return projectStatsDto;
    }

}
