package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeCriteria;
import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengeCriteriaDto;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeCriteriaService;
import com.techlooper.service.ChallengeService;
import org.apache.lucene.queryparser.xml.QueryBuilder;
import org.apache.lucene.queryparser.xml.builders.TermQueryBuilder;
import org.apache.lucene.search.TermQuery;
import org.elasticsearch.index.query.QueryBuilders;
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
  private ChallengeRegistrantRepository challengeRegistrantRepository;

  public ChallengeEntity saveChallengeCriterias(ChallengeCriteriaDto challengeCriteriaDto, String ownerEmail) {
    ChallengeEntity challenge = challengeService.findChallengeIdAndOwnerEmail(challengeCriteriaDto.getChallengeId(), ownerEmail);
    if (challenge == null) {
      return null;
    }

    Set<ChallengeCriteria> criterias = new HashSet<>();
    challengeCriteriaDto.getChallengeCriterias().forEach(criterias::add);
    challenge.setChallengeCriterias(criterias);

    //TODO update to all registrants
    challengeRegistrantRepository.search(QueryBuilders.termQuery("challengeId", challengeCriteriaDto.getChallengeId()))
      .forEach(challengeRegistrantEntity -> {
        challengeRegistrantEntity
      });



    return challengeRepository.save(challenge);
  }
}
