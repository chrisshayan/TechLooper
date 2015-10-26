package com.techlooper.service;

import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeRegistrantPhaseItem;

import java.util.List;
import java.util.Map;

public interface ChallengeRegistrantService {

    Map<ChallengePhaseEnum,ChallengeRegistrantPhaseItem> countNumberOfRegistrantsByPhase(Long challengeId);

}
