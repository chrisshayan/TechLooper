package com.techlooper.service;

import com.techlooper.dto.ChallengeQualificationDto;
import com.techlooper.dto.ChallengeWinnerDto;
import com.techlooper.dto.RejectRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.DraftRegistrantEntity;
import com.techlooper.model.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ChallengeRegistrantService {

    Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> countNumberOfRegistrantsByPhase(Long challengeId);

    Long countNumberOfFinalists(Long challengeId);

    int countNumberOfWinners(Long challengeId);

    Set<ChallengeRegistrantDto> findRegistrantsByChallengeIdAndPhase(Long challengeId, ChallengePhaseEnum phase, String ownerEmail);

    Set<ChallengeRegistrantDto> findWinnerRegistrantsByChallengeId(Long challengeId);

    Set<ChallengeWinner> saveWinner(ChallengeWinnerDto challengeWinner, String loginUser);

    List<ChallengeRegistrantEntity> findRegistrantsByChallengeId(Long challengeId);

    List<ChallengeRegistrantEntity> findRegistrantsByOwner(String ownerEmail);

    ChallengeRegistrantEntity findRegistrantById(Long registrantId);

    ChallengeRegistrantDto rejectRegistrant(String ownerEmail, RejectRegistrantDto rejectRegistrantDto);

    ChallengeRegistrantDto acceptRegistrant(Long registrantId, ChallengePhaseEnum phase);

    List<ChallengeRegistrantDto> qualifyAllRegistrants(String remoteUser, ChallengeQualificationDto challengeQualificationDto);

    Long getNumberOfRegistrants(Long challengeId);

    ChallengeRegistrantDto saveRegistrant(String ownerEmail, ChallengeRegistrantDto challengeRegistrantDto);

    List<ChallengeRegistrantEntity> findChallengeRegistrantWithinPeriod(Long challengeId, Long currentDateTime, TimePeriodEnum period);

    ChallengeRegistrantEntity findRegistrantByChallengeIdAndEmail(Long challengeId, String email);

    DraftRegistrantEntity findDraftRegistrantEntityByChallengeIdAndEmail(Long challengeId, String email, String internalEmail);

    List<ChallengeRegistrantFunnelItem> getChallengeRegistrantFunnel(Long challengeId, String ownerEmail);

    Long getTotalNumberOfRegistrants();

    Set<ChallengeRegistrantDto> getChallengeWinners(Long challengeId);

    List<ChallengeDashBoardInfo> getChallengeDashBoardInfo(JobSeekerDashBoardCriteria criteria);

    DraftRegistrantEntity saveDraftRegistrant(DraftRegistrantEntity draftRegistrantEntity);
}
