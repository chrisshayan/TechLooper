package com.techlooper.service;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.*;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 6/29/15.
 */
public interface ChallengeService {

    ChallengeEntity savePostChallenge(ChallengeDto challengeDto) throws Exception;

    void sendPostChallengeEmailToEmployer(ChallengeEntity challengeEntity)
            throws MessagingException, IOException, TemplateException;

    void sendPostChallengeEmailToTechloopies(ChallengeEntity challengeEntity, Boolean isNewChallenge)
            throws MessagingException, IOException, TemplateException;

    void sendEmailNotifyRegistrantAboutChallengeTimeline(ChallengeEntity challengeEntity,
                                                         ChallengeRegistrantEntity challengeRegistrantEntity, ChallengePhaseEnum challengePhase, boolean isSpecificDayNotification) throws Exception;

    void sendEmailNotifyEmployerWhenPhaseClosed(ChallengeEntity challengeEntity, ChallengePhaseEnum currentPhase,
                                                ChallengePhaseEnum oldPhase) throws Exception;

    ChallengeDetailDto getChallengeDetail(Long challengeId, String loginEmail);

    void sendApplicationEmailToContestant(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity)
            throws MessagingException, IOException, TemplateException;

    ChallengeRegistrantEntity joinChallengeEntity(ChallengeRegistrantDto challengeRegistrantDto);

    long joinChallenge(ChallengeRegistrantDto challengeRegistrantDto);

    List<ChallengeDetailDto> listChallenges();

    List<ChallengeDetailDto> listChallenges(String ownerEmail);

    List<ChallengeEntity> listChallengesByPhase(ChallengePhaseEnum challengePhase);

    Long getTotalNumberOfChallenges();

    Double getTotalAmountOfPrizeValues();

    ChallengeDetailDto getTheLatestChallenge();

    boolean delete(Long id, String ownerEmail);

    ChallengeDto findChallengeById(Long id, String ownerEmail);

    void sendDailySummaryEmailToChallengeOwner(ChallengeEntity challengeEntity) throws Exception;

    boolean isOwnerOfChallenge(String ownerEmail, Long challengeId);

    ChallengeEntity findChallengeIdAndOwnerEmail(Long challengeId, String ownerEmail);

    boolean sendEmailToDailyChallengeRegistrants(String challengeOwner, Long challengeId, Long now, EmailContent emailContent);

    boolean sendEmailToRegistrant(String challengeOwner, Long registrantId, EmailContent emailContent);

    void updateSendEmailToContestantResultCode(ChallengeRegistrantEntity challengeRegistrantEntity, EmailSentResultEnum code);

    void updateSendEmailToChallengeOwnerResultCode(ChallengeEntity challengeEntity, EmailSentResultEnum code);

    void calculateChallengePhases(ChallengeDetailDto challengeDetailDto);

}
