package com.techlooper.controller;

import com.techlooper.model.ChallengeCriteriaDto;
import com.techlooper.model.ChallengeRegistrantCriteriaDto;
import com.techlooper.service.ChallengeCriteriaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Created by phuonghqh on 10/16/15.
 */
@RestController
public class ChallengeCriteriaController {

  @Resource
  private ChallengeCriteriaService challengeCriteriaService;

  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "challenge/criteria", method = RequestMethod.POST)
  public ChallengeCriteriaDto saveChallengeCriteria(@RequestBody ChallengeCriteriaDto challengeCriteriaDto, HttpServletRequest request, HttpServletResponse response) {
    ChallengeCriteriaDto result = challengeCriteriaService.saveChallengeCriteria(challengeCriteriaDto, request.getRemoteUser());
    if (result == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    return result;
  }

  @PreAuthorize("hasAuthority('EMPLOYER')")
  @RequestMapping(value = "challengeRegistrant/criteria", method = RequestMethod.POST)
  public ChallengeRegistrantCriteriaDto saveChallengeRegistrantCriteria(@RequestBody ChallengeRegistrantCriteriaDto registrantCriteriaDto,
                                                                        HttpServletRequest request, HttpServletResponse response) {
    ChallengeRegistrantCriteriaDto result = challengeCriteriaService.saveScoreChallengeRegistrantCriteria(registrantCriteriaDto, request.getRemoteUser());
    if (result == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    return result;
  }


}
