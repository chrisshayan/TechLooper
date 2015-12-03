package com.techlooper.cron;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.EmailSentResultEnum;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeEmailService;
import com.techlooper.service.ChallengeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.techlooper.model.ChallengePhaseEnum.*;
import static com.techlooper.util.DateTimeUtils.*;

public class ChallengePhaseClosedNotifier {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengePhaseClosedNotifier.class);

    @Resource
    private ChallengeService challengeService;

    @Resource
    private ChallengeRepository challengeRepository;

    @Value("${jobAlert.enable}")
    private Boolean enableJobAlert;

    @Resource
    private ChallengeEmailService challengeEmailService;

    @Scheduled(cron = "${scheduled.cron.notifyChallengePhaseClosed}")
    public synchronized void notifyChallengePhaseClosed() throws Exception {
        if (enableJobAlert) {
            List<ChallengePhaseEnum> challengePhases = Arrays.asList(IDEA, UIUX, PROTOTYPE, FINAL, WINNER);

            int count = 0;
            for (ChallengePhaseEnum currentPhase : challengePhases) {
                List<ChallengeEntity> challengeEntities = challengeService.findChallengeByPhase(currentPhase);

                //Thread.sleep(DataUtils.getRandomNumberInRange(300000, 600000));
                for (ChallengeEntity challengeEntity : challengeEntities) {
                    challengeEntity = challengeRepository.findOne(challengeEntity.getChallengeId());
                    ChallengePhaseEnum oldPhase = getPreviousPhase(challengeEntity, currentPhase);
                    if (isClosedPhase(challengeEntity, oldPhase)) {
                        if (StringUtils.isEmpty(challengeEntity.getLastEmailSentDateTime())) {
                            challengeEntity.setLastEmailSentDateTime(yesterdayDate(BASIC_DATE_TIME_PATTERN));
                        }

                        Date lastSentDate = string2Date(challengeEntity.getLastEmailSentDateTime(), BASIC_DATE_TIME_PATTERN);
                        Date currentDate = new Date();
                        if (daysBetween(lastSentDate, currentDate) > 0) {
                            try {
                                challengeEmailService.sendEmailNotifyEmployerWhenPhaseClosed(challengeEntity, currentPhase, oldPhase);
                                challengeEmailService.updateSendEmailToChallengeOwnerResultCode(challengeEntity, EmailSentResultEnum.OK);
                                count++;
                            } catch (Exception ex) {
                                LOGGER.error(ex.getMessage(), ex);
                                challengeEmailService.updateSendEmailToChallengeOwnerResultCode(challengeEntity, EmailSentResultEnum.ERROR);
                            }
                        }
                    }

                }
            }
            LOGGER.info("There are " + count + " emails has been sent to notify employer as soon as phase closed");
        }
    }

    private ChallengePhaseEnum getPreviousPhase(ChallengeEntity challengeEntity, ChallengePhaseEnum currentPhase) {
        switch (currentPhase) {
            case IDEA:
                return REGISTRATION;
            case UIUX:
                if (StringUtils.isNotEmpty(challengeEntity.getIdeaSubmissionDateTime())) {
                    return IDEA;
                } else {
                    return REGISTRATION;
                }
            case PROTOTYPE:
                if (StringUtils.isNotEmpty(challengeEntity.getUxSubmissionDateTime())) {
                    return UIUX;
                } else if (StringUtils.isNotEmpty(challengeEntity.getIdeaSubmissionDateTime())) {
                    return IDEA;
                } else {
                    return REGISTRATION;
                }
            case IN_PROGRESS:
                return REGISTRATION;
            case FINAL:
                if (StringUtils.isNotEmpty(challengeEntity.getPrototypeSubmissionDateTime())) {
                    return PROTOTYPE;
                } else if (StringUtils.isNotEmpty(challengeEntity.getUxSubmissionDateTime())) {
                    return UIUX;
                } else if (StringUtils.isNotEmpty(challengeEntity.getIdeaSubmissionDateTime())) {
                    return IDEA;
                } else {
                    return REGISTRATION;
                }
            case WINNER:
                return FINAL;
            default:
                return null;
        }
    }

    private boolean isClosedPhase(ChallengeEntity challengeEntity, ChallengePhaseEnum oldPhase) {
        final int TIME_DISTANCE = 1;
        try {
            switch (oldPhase) {
                case IDEA:
                    return daysBetween(challengeEntity.getIdeaSubmissionDateTime(), currentDate()) == TIME_DISTANCE;
                case UIUX:
                    return daysBetween(challengeEntity.getUxSubmissionDateTime(), currentDate()) == TIME_DISTANCE;
                case PROTOTYPE:
                    return daysBetween(challengeEntity.getPrototypeSubmissionDateTime(), currentDate()) == TIME_DISTANCE;
                case IN_PROGRESS:
                    return daysBetween(challengeEntity.getRegistrationDateTime(), currentDate()) == TIME_DISTANCE;
                case FINAL:
                    return daysBetween(challengeEntity.getSubmissionDateTime(), currentDate()) == TIME_DISTANCE;
                default:
                    return false;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return false;
    }

}
