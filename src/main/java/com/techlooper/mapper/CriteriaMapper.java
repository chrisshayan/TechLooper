package com.techlooper.mapper;

import com.techlooper.entity.ChallengeCriteria;
import com.techlooper.entity.ChallengeRegistrantCriteria;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by phuonghqh on 2/1/16.
 */
@Mapper
@Repository
public interface CriteriaMapper {
  ChallengeRegistrantCriteria fromChallenge(ChallengeCriteria criteria);
  Set<ChallengeRegistrantCriteria> fromChallenge(Set<ChallengeCriteria> criteria);
}
