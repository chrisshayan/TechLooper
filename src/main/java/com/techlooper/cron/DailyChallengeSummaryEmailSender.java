package com.techlooper.cron;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.EmailSentResultEnum;
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

import static com.techlooper.util.DateTimeUtils.*;

public class DailyChallengeSummaryEmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(DailyChallengeSummaryEmailSender.class);

    @Resource
    private ChallengeService challengeService;

    @Value("${jobAlert.enable}")
    private Boolean enableJobAlert;

    @Scheduled(cron = "${scheduled.cron.dailyChallengeSummary}")
    public synchronized void sendDailyEmailAboutChallengeSummary() throws Exception {
        if (enableJobAlert) {
            List<ChallengePhaseEnum> challengePhases = Arrays.asList(ChallengePhaseEnum.REGISTRATION, ChallengePhaseEnum.IN_PROGRESS);

            int count = 0;
            for (ChallengePhaseEnum challengePhase : challengePhases) {
                List<ChallengeEntity> challengeEntities = challengeService.listChallengesByPhase(challengePhase);

                for (ChallengeEntity challengeEntity : challengeEntities) {
                    try {
                        if (StringUtils.isEmpty(challengeEntity.getLastEmailSentDateTime())) {
                            challengeEntity.setLastEmailSentDateTime(yesterdayDate(BASIC_DATE_TIME_PATTERN));
                        }

                        Date lastSentDate = string2Date(challengeEntity.getLastEmailSentDateTime(), BASIC_DATE_TIME_PATTERN);
                        Date currentDate = new Date();
                        if (daysBetween(lastSentDate, currentDate) > 0) {
                            challengeService.sendDailySummaryEmailToChallengeOwner(challengeEntity);
                            challengeService.updateSendEmailToChallengeOwnerResultCode(challengeEntity, EmailSentResultEnum.OK);
                            count++;
                        }
                    } catch (Exception ex) {
                        challengeService.updateSendEmailToChallengeOwnerResultCode(challengeEntity, EmailSentResultEnum.ERROR);
                        LOGGER.error(ex.getMessage(), ex);
                    }
                }
            }
            LOGGER.info("There are " + count + " daily emails has been sent about challenge summary");
        }
    }
}
