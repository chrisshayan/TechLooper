package com.techlooper.service.impl;

import com.techlooper.dto.DashBoardInfo;
import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.vnw.VnwUserRepo;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.EmployerService;
import com.techlooper.service.ProjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 8/11/15.
 */
@Service
public class EmployerServiceImpl implements EmployerService {

  @Resource
  private ChallengeService challengeService;

  @Resource
  private ProjectService projectService;

  @Resource
  private VnwUserRepo vnwUserRepo;

  @Resource
  private ChallengeRegistrantRepository challengeRegistrantRepository;

  public DashBoardInfo getDashboardInfo(String owner) {
    VnwUser user = vnwUserRepo.findByUsernameIgnoreCase(owner);
    String email = user.getEmail();
    return DashBoardInfo.DashBoardInfoBuilder.dashBoardInfo()
      .withProjects(projectService.findByOwner(email))
      .withChallenges(challengeService.findInProgressChallenges(email))
      .build();
  }
}
