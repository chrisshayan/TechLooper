package com.techlooper.service;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.ChallengeSubmissionEntity;
import com.techlooper.model.*;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

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
                                                       ChallengeRegistrantEntity challengeRegistrantEntity, ChallengePhaseEnum challengePhase) throws Exception;

  ChallengeDetailDto getChallengeDetail(Long challengeId);

  Long getNumberOfRegistrants(Long challengeId);

  void sendApplicationEmailToContestant(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity)
    throws MessagingException, IOException, TemplateException;

  void sendApplicationEmailToEmployer(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity)
    throws MessagingException, IOException, TemplateException;

  long joinChallenge(ChallengeRegistrantDto challengeRegistrantDto) throws MessagingException, IOException, TemplateException;

  List<ChallengeDetailDto> listChallenges();

  List<ChallengeDetailDto> listChallenges(String ownerEmail);

  List<ChallengeEntity> listChallengesByPhase(ChallengePhaseEnum challengePhase);

  Long getTotalNumberOfChallenges();

  Double getTotalAmountOfPrizeValues();

  Long getTotalNumberOfRegistrants();

  ChallengeDetailDto getTheLatestChallenge();

  Collection<ChallengeDetailDto> findByOwnerAndCondition(String owner, Predicate<? super ChallengeEntity> condition);

  Collection<ChallengeDetailDto> findInProgressChallenges(String owner);

//  Collection<ChallengeRegistrantDto> findRegistrantsByChallengeId(Long challengeId);

  Long countRegistrantsByChallengeId(Long challengeId);

  boolean delete(Long id, String ownerEmail);

  ChallengeDto findChallengeById(Long id);

  Set<ChallengeRegistrantDto> findRegistrantsByOwner(RegistrantFilterCondition condition) throws ParseException;

  ChallengeRegistrantDto saveRegistrant(String ownerEmail, ChallengeRegistrantDto challengeRegistrantDto);

  List<ChallengeRegistrantEntity> findChallengeRegistrantWithinPeriod(
    Long challengeId, Long currentDateTime, TimePeriodEnum period);

  List<ChallengeRegistrantEntity> filterChallengeRegistrantByDate(RegistrantFilterCondition condition) throws ParseException;

  List<ChallengeSubmissionEntity> findChallengeSubmissionWithinPeriod(
    Long challengeId, Long currentDateTime, TimePeriodEnum period);

  void sendDailySummaryEmailToChallengeOwner(ChallengeEntity challengeEntity) throws Exception;

  boolean isOwnerOfChallenge(String ownerEmail, Long challengeId);

  boolean sendEmailToDailyChallengeRegistrants(String challengeOwner, Long challengeId, Long now, EmailContent emailContent);

  boolean sendEmailToRegistrant(String challengeOwner, Long challengeId, Long registrantId, EmailContent emailContent);

  List<ChallengeSubmissionDto> findChallengeSubmissionByRegistrant(Long challengeId, Long registrantId);

  void updateSendEmailToContestantResultCode(ChallengeRegistrantEntity challengeRegistrantEntity, EmailSentResultEnum code);

  void updateSendEmailToChallengeOwnerResultCode(ChallengeEntity challengeEntity, EmailSentResultEnum code);

  Set<Long> findRegistrantByChallengeSubmissionDate(Long challengeId, String fromDate, String toDate);

  ChallengeRegistrantDto acceptRegistrant(String ownerEmail, Long registrantId);

}
