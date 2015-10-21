package com.techlooper.service;

import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.model.ChallengeSubmissionDto;

/**
 * Created by phuonghqh on 10/9/15.
 */
public interface ChallengeSubmissionService {

  ChallengeSubmissionEntity submitMyResult(ChallengeSubmissionDto challengeSubmissionDto);


}
