package com.techlooper.controller;

import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.model.ChallengeSubmissionDto;
import com.techlooper.service.ChallengeSubmissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 10/9/15.
 */
@RestController
public class ChallengeSubmissionController {

  @Resource
  private ChallengeSubmissionService challengeSubmissionService;

  @RequestMapping(value = "user/challengeSubmission", method = RequestMethod.POST)
  public ChallengeSubmissionEntity submitMyResult(@RequestBody ChallengeSubmissionDto challengeSubmissionDto) {
    return challengeSubmissionService.submitMyResult(challengeSubmissionDto);
  }

}
