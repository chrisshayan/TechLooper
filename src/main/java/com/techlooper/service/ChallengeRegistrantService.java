package com.techlooper.service;

import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeRegistrantPhaseItem;

import java.util.Map;
import java.util.Set;

public interface ChallengeRegistrantService {

  Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> countNumberOfRegistrantsByPhase(Long challengeId);

  Long countNumberOfWinners(Long challengeId);

  Set<ChallengeRegistrantDto> findRegistrantsByChallengeIdAndPhase(Long challengeId, ChallengePhaseEnum phase, String ownerEmail);
}
