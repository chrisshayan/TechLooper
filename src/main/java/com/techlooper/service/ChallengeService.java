package com.techlooper.service;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.model.ChallengeDetailDto;
import com.techlooper.model.ChallengeDto;
import com.techlooper.model.ChallengePhaseEnum;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
public interface ChallengeService {

    ChallengeEntity postChallenge(ChallengeDto challengeDto);

    Long joinChallenge(ChallengeRegistrantDto challengeRegistrantDto);

    ChallengeDetailDto getChallengeDetail(Long challengeId, String loginEmail);

    List<ChallengeDetailDto> listChallenges();

    List<ChallengeDetailDto> listChallenges(String ownerEmail);

    List<ChallengeEntity> listChallengesByPhase(ChallengePhaseEnum challengePhase);

    Long getTotalNumberOfChallenges();

    Double getTotalAmountOfPrizeValues();

    ChallengeDetailDto getTheLatestChallenge();

    boolean deleteChallenge(Long challengeId, String ownerEmail);

    boolean isOwnerOfChallenge(String ownerEmail, Long challengeId);

    ChallengeEntity findChallengeById(Long challengeId, String ownerEmail);

    ChallengeEntity findChallengeById(Long challengeId);

    void calculateChallengePhases(ChallengeDetailDto challengeDetailDto);

}
