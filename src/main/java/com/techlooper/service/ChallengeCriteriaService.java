package com.techlooper.service;

import com.techlooper.model.ChallengeCriteriaDto;
import com.techlooper.model.ChallengeRegistrantCriteriaDto;

/**
 * Created by phuonghqh on 10/16/15.
 */
public interface ChallengeCriteriaService {

  ChallengeCriteriaDto saveChallengeCriteria(ChallengeCriteriaDto challengeCriteriaDto, String owner);

  ChallengeRegistrantCriteriaDto saveScoreChallengeRegistrantCriteria(ChallengeRegistrantCriteriaDto registrantCriteriaDto, String ownerEmail);

  ChallengeRegistrantCriteriaDto findByChallengeRegistrantId(Long registrantId, String ownerEmail);
}
