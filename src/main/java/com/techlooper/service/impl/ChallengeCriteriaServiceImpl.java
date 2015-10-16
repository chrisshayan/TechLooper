package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeCriteria;
import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengeCriteriaDto;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeCriteriaService;
import com.techlooper.service.ChallengeService;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by phuonghqh on 10/16/15.
 */
@Service
public class ChallengeCriteriaServiceImpl implements ChallengeCriteriaService {

  @Resource
  private ChallengeRepository challengeRepository;

  @Resource
  private ChallengeService challengeService;

  @Resource
  private Mapper dozerMapper;

  public ChallengeEntity saveChallengeCriterias(ChallengeCriteriaDto challengeCriteriaDto, String ownerEmail) {
    ChallengeEntity challenge = challengeService.findChallengeIdAndOwnerEmail(challengeCriteriaDto.getChallengeId(), ownerEmail);
    if (challenge == null) {
      return null;
    }

    Set<ChallengeCriteria> criterias = new HashSet<>();
    challengeCriteriaDto.getChallengeCriterias().forEach(criterias::add);
    challenge.setChallengeCriterias(criterias);
    return challengeRepository.save(challenge);
  }
}
