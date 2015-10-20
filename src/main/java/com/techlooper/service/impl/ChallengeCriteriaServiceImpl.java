package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeCriteria;
import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantCriteria;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengeCriteriaDto;
import com.techlooper.model.ChallengeRegistrantCriteriaDto;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeCriteriaService;
import com.techlooper.service.ChallengeService;
import com.techlooper.util.DataUtils;
import org.dozer.Mapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Iterator;
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
  private ChallengeRegistrantRepository challengeRegistrantRepository;

  @Resource
  private Mapper dozerMapper;

  public ChallengeCriteriaDto saveChallengeCriteria(ChallengeCriteriaDto challengeCriteriaDto, String ownerEmail) {
    ChallengeEntity challenge = challengeService.findChallengeIdAndOwnerEmail(challengeCriteriaDto.getChallengeId(), ownerEmail);
    if (challenge == null) {
      return challengeCriteriaDto;
    }

    Set<ChallengeCriteria> criteria = new HashSet<>();
    challengeCriteriaDto.getChallengeCriteria().forEach(cri -> {
      if (cri.getCriteriaId() == null) cri.setCriteriaId(DataUtils.generateStringId());
      criteria.add(cri);
    });

    Set<ChallengeRegistrantEntity> challengeRegistrantEntities = new HashSet<>();
    Iterator<ChallengeRegistrantEntity> challengeIterator = challengeRegistrantRepository.search(
      QueryBuilders.termQuery("challengeId", challengeCriteriaDto.getChallengeId())).iterator();

    while (challengeIterator.hasNext()) {
      final ChallengeRegistrantEntity registrantEntity = challengeIterator.next();
      final Set<ChallengeRegistrantCriteria> registrantCriteria = new HashSet<>();

      if (registrantEntity.getRegistrantId() == 27L) {
        System.out.println(27);
      }
      criteria.forEach(challengeCri -> {
        ChallengeRegistrantCriteria registrantCri = registrantEntity.getCriteria().stream()
          .filter(cri -> challengeCri.getCriteriaId().equals(cri.getCriteriaId()))
          .findFirst().orElse(new ChallengeRegistrantCriteria());
        dozerMapper.map(challengeCri, registrantCri);
        registrantCriteria.add(registrantCri);
        registrantEntity.getCriteria().remove(registrantCri);
      });
      registrantEntity.setCriteria(registrantCriteria);
      challengeRegistrantEntities.add(registrantEntity);
      challengeCriteriaDto.getRegistrantCriteria().add(dozerMapper.map(registrantEntity, ChallengeRegistrantCriteriaDto.class));
    }

    challenge.setCriteria(criteria);
    challengeRegistrantRepository.save(challengeRegistrantEntities);
    challengeRepository.save(challenge);
    return challengeCriteriaDto;
  }
}
