package com.techlooper.service.impl;

import com.techlooper.entity.ProjectEntity;
import com.techlooper.model.ProjectDto;
import com.techlooper.repository.elasticsearch.ProjectRepository;
import com.techlooper.service.ProjectService;
import org.dozer.Mapper;
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
  private Mapper dozerMapper;

  @Override
  public ProjectEntity saveProject(ProjectDto projectDto) {
    ProjectEntity projectEntity = dozerMapper.map(projectDto, ProjectEntity.class);
    Date currentDate = new Date();
    projectEntity.setProjectId(currentDate.getTime());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

  public ProjectDto findById(Long id) {
    return dozerMapper.map(projectRepository.findOne(id), ProjectDto.class);
  }

}
