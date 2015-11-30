package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeRegistrantPhaseItem;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeRegistrantService;
import com.techlooper.service.ReportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by phuonghqh on 11/30/15.
 */
@Service
public class ReportServiceImpl implements ReportService {

  @Resource
  private ChallengeRepository challengeRepository;

  @Resource
  private ChallengeRegistrantService challengeRegistrantService;

  public InputStream generateFinalChallengeReport(String challengeAuthorEmail, Long challengeId) {
    ChallengeEntity challenge = challengeRepository.findOne(challengeId);
    if (challenge == null || !challenge.getAuthorEmail().equalsIgnoreCase(challengeAuthorEmail)) {
      return null;
    }

    Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> registrantAgg = challengeRegistrantService.countNumberOfRegistrantsByPhase(challengeId);
    Set<ChallengePhaseEnum> redundantPhases = registrantAgg.keySet().stream().filter(phase -> !challenge.hasPhase(phase)).collect(Collectors.toSet());
    registrantAgg.keySet().removeAll(redundantPhases);
    System.out.println(registrantAgg);
    return null;
  }
}
