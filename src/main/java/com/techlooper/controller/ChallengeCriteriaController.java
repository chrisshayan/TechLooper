package com.techlooper.controller;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengeCriteriaDto;
import com.techlooper.service.ChallengeCriteriaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by phuonghqh on 10/16/15.
 */
@RestController
public class ChallengeCriteriaController {

  @Resource
  private ChallengeCriteriaService challengeCriteriaService;

  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "challenge/criteria", method = RequestMethod.POST)
  public ChallengeEntity save(ChallengeCriteriaDto challengeCriteriaDto, HttpServletRequest request) {
    return challengeCriteriaService.saveChallengeCriterias(challengeCriteriaDto, request.getRemoteUser());
  }
}
