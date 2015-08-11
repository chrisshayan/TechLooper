package com.techlooper.service.impl;

import com.techlooper.dto.DashBoardInfo;
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

  public DashBoardInfo getDashboardInfo() {

    return null;
  }
}
