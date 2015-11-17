package com.techlooper.service;

import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeSubmissionDto;
import com.techlooper.model.ChallengeSubmissionPhaseItem;
import com.techlooper.model.TimePeriodEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by phuonghqh on 10/9/15.
 */
public interface ChallengeSubmissionService {

    ChallengeSubmissionEntity submitMyResult(ChallengeSubmissionDto challengeSubmissionDto);

    Map<ChallengePhaseEnum, ChallengeSubmissionPhaseItem> countNumberOfSubmissionsByPhase(Long challengeId, Boolean isRead);

    void markChallengeSubmissionAsRead(ChallengeSubmissionDto challengeSubmissionDto);

    ChallengeSubmissionEntity findChallengeSubmissionByRegistrantPhase(Long registrantId, ChallengePhaseEnum phase);

    List<ChallengeSubmissionEntity> findChallengeSubmissionWithinPeriod(Long challengeId, Long currentDateTime, TimePeriodEnum period);
}
