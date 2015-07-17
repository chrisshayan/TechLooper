package com.techlooper.service.impl;

import com.techlooper.entity.EmployerEntity;
import com.techlooper.entity.ProjectEntity;
import com.techlooper.model.Employer;
import com.techlooper.model.EmployerDto;
import com.techlooper.model.ProjectDetailDto;
import com.techlooper.model.ProjectDto;
import com.techlooper.repository.elasticsearch.CompanySearchResultRepository;
import com.techlooper.repository.elasticsearch.ProjectRepository;
import com.techlooper.service.ProjectService;
import org.dozer.Mapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 7/10/15.
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private CompanySearchResultRepository companySearchResultRepository;

    @Resource
    private Mapper dozerMapper;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public ProjectEntity saveProject(ProjectDto projectDto) {
        ProjectEntity projectEntity = dozerMapper.map(projectDto, ProjectEntity.class);
        Date currentDate = new Date();
        projectEntity.setProjectId(currentDate.getTime());
        projectEntity.setCreatedDate(simpleDateFormat.format(currentDate));
        return projectRepository.save(projectEntity);
    }

    @Override
    public List<ProjectDto> listProject() {
        List<ProjectDto> projects = new ArrayList<>();
        Iterator<ProjectEntity> projectIterator = projectRepository.findAll().iterator();
        while (projectIterator.hasNext()) {
            ProjectEntity projectEntity = projectIterator.next();
            ProjectDto projectDto = dozerMapper.map(projectEntity, ProjectDto.class);
            projects.add(projectDto);
        }
        return projects;
    }

    @Override
    public ProjectDetailDto getProjectDetail(Long projectId) {
        ProjectDetailDto projectDetail = new ProjectDetailDto();
        ProjectEntity projectEntity = projectRepository.findOne(projectId);
        ProjectDto project = dozerMapper.map(projectEntity, ProjectDto.class);
        projectDetail.setProject(project);

        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(QueryBuilders.nestedQuery("employers",
                QueryBuilders.matchPhraseQuery("employers.userName", project.getAuthorEmail())));
        List<EmployerEntity> employerEntities = companySearchResultRepository.search(searchQueryBuilder.build()).getContent();

        if (!employerEntities.isEmpty()) {
            EmployerEntity employerEntity = employerEntities.get(0);
            EmployerDto employerDto = dozerMapper.map(employerEntity, EmployerDto.class);
            if (!employerEntity.getEmployers().isEmpty()) {
                Employer employer = employerEntity.getEmployers().get(0);
                employerDto.setCreatedDate(simpleDateFormat.format(employer.getCreatedDate()));
            }
            projectDetail.setCompany(employerDto);
        }
        return projectDetail;
    }

}
