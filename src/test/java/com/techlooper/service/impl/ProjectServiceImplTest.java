package com.techlooper.service.impl;

import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.config.ProjectServiceConfigurationTest;
import com.techlooper.entity.ProjectEntity;
import com.techlooper.model.ProjectDetailDto;
import com.techlooper.model.ProjectDto;
import com.techlooper.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchConfiguration.class, ProjectServiceConfigurationTest.class})
public class ProjectServiceImplTest {

    @Resource
    private ProjectService projectService;

    @Test
    public void testSaveProject() throws Exception {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setAuthorEmail("khoa.nguyen@navigosgroup.com");
        projectDto.setProjectTitle("Test Service");
        projectDto.setProjectDescription("Test Service Description");
        projectDto.setSkills(Arrays.asList("Java", "Spring"));
        projectDto.setPayMethod("Fixed Price");
        projectDto.setEstimatedEndDate("13/07/2015");
        projectDto.setBudget(100D);
        projectDto.setNumberOfHires(30L);
        projectDto.setEstimatedDuration("More than 6 months");
        projectDto.setEstimatedWorkload("More than 30 hours/week");
        projectDto.setHourlyRate(25D);
        ProjectEntity projectEntity = projectService.saveProject(projectDto);
        assertTrue(projectEntity.getProjectId() != null);
        assertTrue(projectEntity.getCreatedDate() != null);
    }

    @Test
    public void testGetProjectDetail() throws Exception {
        Long projectId = 1436867134185L;
        ProjectDetailDto projectDetailDto = projectService.getProjectDetail(projectId);
        assertTrue(projectDetailDto.getProject() != null);
        assertTrue(projectDetailDto.getCompany() != null);
    }
}