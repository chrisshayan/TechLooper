package com.techlooper.service.impl;

import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.entity.ChallengeSubmissionEntity.ChallengeSubmissionEntityBuilder;
import com.techlooper.model.ChallengeSubmissionDto;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeSubmissionRepository;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.ChallengeSubmissionService;
import com.techlooper.util.DateTimeUtils;
import org.dozer.Mapper;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by phuonghqh on 10/9/15.
 */
@Service
public class ChallengeSubmissionServiceImpl implements ChallengeSubmissionService {

  @Resource
  private ChallengeRegistrantRepository challengeRegistrantRepository;

  @Resource
  private ChallengeService challengeService;

  @Resource
  private Mapper dozerMapper;

  @Resource
  private ChallengeSubmissionRepository challengeSubmissionRepository;

  public ChallengeSubmissionEntity submitMyResult(ChallengeSubmissionDto challengeSubmissionDto) {
    MatchQueryBuilder registrantEmailQuery = matchQuery("registrantEmail", challengeSubmissionDto.getRegistrantEmail()).minimumShouldMatch("100%");
    TermQueryBuilder challengeQuery = termQuery("challengeId", challengeSubmissionDto.getChallengeId());
    Iterator<ChallengeRegistrantEntity> registrantIterator = challengeRegistrantRepository.search(
      boolQuery().must(registrantEmailQuery).must(challengeQuery)).iterator();

    ChallengeRegistrantEntity registrant = registrantIterator.hasNext() ? registrantIterator.next() :
      challengeService.joinChallengeEntity(dozerMapper.map(challengeSubmissionDto, ChallengeRegistrantDto.class));

    ChallengeSubmissionEntity challengeSubmissionEntity = dozerMapper.map(challengeSubmissionDto, ChallengeSubmissionEntity.class);
    ChallengeSubmissionEntityBuilder.challengeSubmissionEntity(challengeSubmissionEntity)
      .withChallengeSubmissionId(DateTime.now().getMillis())
      .withRegistrantId(registrant.getRegistrantId())
      .withRegistrantName(String.format("%s %s", registrant.getRegistrantFirstName(), registrant.getRegistrantLastName()))
      .withSubmissionDateTime(DateTimeUtils.currentDate());

    return challengeSubmissionRepository.save(challengeSubmissionEntity);
  }
}
