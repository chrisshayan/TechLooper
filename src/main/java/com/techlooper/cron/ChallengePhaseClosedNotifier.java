package com.techlooper.cron;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.EmailSentResultEnum;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.service.ChallengeRegistrantService;
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
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Value("${jobAlert.enable}")
    private Boolean enableJobAlert;

    @Scheduled(cron = "${scheduled.cron.notifyChallengePhaseClosed}")
    public synchronized void notifyChallengePhaseClosed() throws Exception {
        if (enableJobAlert) {
            List<ChallengePhaseEnum> challengePhases = Arrays.asList(REGISTRATION, IDEA, UIUX, PROTOTYPE, FINAL);

            int count = 0;
            for (ChallengePhaseEnum challengePhase : challengePhases) {
                List<ChallengeEntity> challengeEntities = challengeService.listChallengesByPhase(challengePhase);

                for (ChallengeEntity challengeEntity : challengeEntities) {
                    if (isClosedPhase(challengeEntity, challengePhase)) {
                        if (StringUtils.isEmpty(challengeEntity.getLastEmailSentDateTime())) {
                            challengeEntity.setLastEmailSentDateTime(yesterdayDate(BASIC_DATE_TIME_PATTERN));
                        }

                        Date lastSentDate = string2Date(challengeEntity.getLastEmailSentDateTime(), BASIC_DATE_TIME_PATTERN);
                        Date currentDate = new Date();
                        if (daysBetween(lastSentDate, currentDate) > 0) {
                            try {
                                challengeService.sendEmailNotifyEmployerWhenPhaseClosed(challengeEntity, challengePhase);
                                challengeService.updateSendEmailToChallengeOwnerResultCode(challengeEntity, EmailSentResultEnum.OK);
                                count++;
                            } catch (Exception ex) {
                                LOGGER.error(ex.getMessage(), ex);
                                challengeService.updateSendEmailToChallengeOwnerResultCode(challengeEntity, EmailSentResultEnum.ERROR);
                            }
                        }
                    }

                }
            }
            LOGGER.info("There are " + count + " emails has been sent to notify employer as soon as phase closed");
        }
    }

    private boolean isClosedPhase(ChallengeEntity challengeEntity, ChallengePhaseEnum challengePhase) {
        final int TIME_DISTANCE = 1;
        try {
            if (challengePhase == ChallengePhaseEnum.REGISTRATION) {
                return daysBetween(challengeEntity.getRegistrationDateTime(), currentDate()) == TIME_DISTANCE;
            } else if (challengePhase == ChallengePhaseEnum.IDEA) {
                return daysBetween(challengeEntity.getIdeaSubmissionDateTime(), currentDate()) == TIME_DISTANCE;
            } else if (challengePhase == ChallengePhaseEnum.UIUX) {
                return daysBetween(challengeEntity.getUxSubmissionDateTime(), currentDate()) == TIME_DISTANCE;
            } else if (challengePhase == ChallengePhaseEnum.PROTOTYPE) {
                return daysBetween(challengeEntity.getPrototypeSubmissionDateTime(), currentDate()) == TIME_DISTANCE;
            } else if (challengePhase == ChallengePhaseEnum.IN_PROGRESS) {
                return daysBetween(challengeEntity.getSubmissionDateTime(), currentDate()) == TIME_DISTANCE;
            } else if (challengePhase == ChallengePhaseEnum.FINAL) {
                return daysBetween(challengeEntity.getSubmissionDateTime(), currentDate()) == TIME_DISTANCE;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return false;
    }

}
