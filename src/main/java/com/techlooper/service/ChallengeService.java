package com.techlooper.service;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.model.ChallengeDetailDto;
import com.techlooper.model.ChallengeDto;
import com.techlooper.model.ChallengeFilterCondition;
import com.techlooper.model.ChallengePhaseEnum;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
public interface ChallengeService {

    ChallengeEntity postChallenge(ChallengeDto challengeDto);

    Long joinChallenge(ChallengeRegistrantDto challengeRegistrantDto);

    ChallengeDetailDto getChallengeDetail(Long challengeId, String loginEmail);

    List<ChallengeDetailDto> findChallenges(ChallengeFilterCondition allChallengeFilterCondition);

    List<ChallengeDetailDto> findChallengeByOwner(String ownerEmail);

    List<ChallengeEntity> findChallengeByPhase(ChallengePhaseEnum challengePhase);

    Long getNumberOfChallenges();

    Double getTotalAmountOfPrizeValues();

    ChallengeDetailDto getTheLatestChallenge();

    boolean deleteChallenge(Long challengeId, String ownerEmail);

    boolean isChallengeOwner(String ownerEmail, Long challengeId);

    ChallengeEntity findChallengeById(Long challengeId, String ownerEmail);

    ChallengeEntity findChallengeById(Long challengeId);

    void calculateChallengePhases(ChallengeDetailDto challengeDetailDto);

    ChallengeDetailDto updateVisibleWinner(ChallengeDetailDto challengeDetailDto, String owner);
}
