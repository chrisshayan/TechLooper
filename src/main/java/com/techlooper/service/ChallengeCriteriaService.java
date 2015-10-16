package com.techlooper.service;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengeCriteriaDto;

/**
 * Created by phuonghqh on 10/16/15.
 */
public interface ChallengeCriteriaService {

  ChallengeEntity saveChallengeCriterias(ChallengeCriteriaDto challengeCriteriaDto, String owner);
}
