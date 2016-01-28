package com.techlooper.service;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.entity.DraftRegistrantEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.EmailContent;
import com.techlooper.model.EmailSentResultEnum;

public interface ChallengeEmailService {

    final Long ANNOUNCE_WINNER_EMAIL_TEMPLATE_EN = 4L;

    final Long ANNOUNCE_WINNER_EMAIL_TEMPLATE_VI = 104L;

    final String VAR_CONTEST_NAME = "{contest_name}";

    final String VAR_CONTEST_FIRST_PRIZE = "{contest_1st_prize}";

    final String VAR_CONTESTANT_FIRST_NAME = "{contestant_first_name}";

    final String VAR_MAIL_SIGNATURE = "{mail_signature}";

    final String VAR_CONTEST_SECOND_PRIZE = "{contest_2nd_prize}";

    final String VAR_CONTEST_THIRD_PRIZE = "{contest_3rd_prize}";

    final String VAR_FIRST_WINNER_FIRST_NAME = "{1st_winner_firstname}";

    final String VAR_FIRST_WINNER_FULL_NAME = "{1st_winner_fullname}";

    final String VAR_SECOND_WINNER_FIRST_NAME = "{2nd_winner_firstname}";

    final String VAR_SECOND_WINNER_FULL_NAME = "{2nd_winner_fullname}";

    final String VAR_THIRD_WINNER_FIRST_NAME = "{3rd_winner_firstname}";

    final String VAR_THIRD_WINNER_FULL_NAME = "{3rd_winner_fullname}";

    boolean sendFeedbackEmail(EmailContent emailContent);

    void sendPostChallengeEmailToEmployer(ChallengeEntity challengeEntity);

    void sendEmailNotifyEmployerUpdateCriteria(ChallengeEntity challengeEntity);

    void sendPostChallengeEmailToTechloopies(ChallengeEntity challengeEntity, Boolean isNewChallenge);

    void sendEmailNotifyRegistrantAboutChallengeTimeline(ChallengeEntity challengeEntity,
                                                         ChallengeRegistrantEntity challengeRegistrantEntity, ChallengePhaseEnum challengePhase, boolean isSpecificDayNotification) throws Exception;

    void sendEmailNotifyEmployerWhenPhaseClosed(ChallengeEntity challengeEntity,
                                                ChallengePhaseEnum currentPhase, ChallengePhaseEnum oldPhase);

    void sendApplicationEmailToContestant(ChallengeEntity challengeEntity, ChallengeRegistrantEntity challengeRegistrantEntity);

    void sendDailySummaryEmailToChallengeOwner(ChallengeEntity challengeEntity);

    void updateSendEmailToContestantResultCode(ChallengeRegistrantEntity challengeRegistrantEntity, EmailSentResultEnum code);

    void updateSendEmailToChallengeOwnerResultCode(ChallengeEntity challengeEntity, EmailSentResultEnum code);

    boolean sendEmailToDailyChallengeRegistrants(String challengeOwner, Long challengeId, Long now, EmailContent emailContent);

    boolean sendEmailToRegistrant(String challengeOwner, Long registrantId, EmailContent emailContent);

    void sendEmailNotifyRegistrantWhenQualified(ChallengeRegistrantEntity challengeRegistrantEntity);

    void sendEmailToVerifyRegistrantOfInternalChallenge(DraftRegistrantEntity draftRegistrantEntity);
}
