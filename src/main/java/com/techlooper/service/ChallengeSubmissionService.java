package com.techlooper.service;

import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeSubmissionDto;
import com.techlooper.model.ChallengeSubmissionPhaseItem;

import java.util.Map;

/**
 * Created by phuonghqh on 10/9/15.
 */
public interface ChallengeSubmissionService {

    ChallengeSubmissionEntity submitMyResult(ChallengeSubmissionDto challengeSubmissionDto);

    Map<ChallengePhaseEnum, ChallengeSubmissionPhaseItem> countNumberOfSubmissionsByPhase(Long challengeId);

}
