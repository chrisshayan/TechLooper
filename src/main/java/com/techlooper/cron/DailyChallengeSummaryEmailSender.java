package com.techlooper.cron;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.service.ChallengeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class DailyChallengeSummaryEmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(DailyChallengeSummaryEmailSender.class);

    @Resource
    private ChallengeService challengeService;

    @Value("${jobAlert.enable}")
    private Boolean enableJobAlert;

    @Scheduled(cron = "${scheduled.cron.dailyChallengeSummary}")
    public void sendDailyEmailAboutChallengeSummary() throws Exception {
        if (enableJobAlert) {
            List<ChallengePhaseEnum> challengePhases = Arrays.asList(ChallengePhaseEnum.REGISTRATION, ChallengePhaseEnum.IN_PROGRESS);

            int count = 0;
            for (ChallengePhaseEnum challengePhase : challengePhases) {
                List<ChallengeEntity> challengeEntities = challengeService.listChallengesByPhase(challengePhase);

                for (ChallengeEntity challengeEntity : challengeEntities) {
                    try {
                        challengeService.sendDailySummaryEmailToChallengeOwner(challengeEntity);
                        count++;
                    } catch (Exception ex) {
                        LOGGER.error(ex.getMessage(), ex);
                    }
                }
            }
            LOGGER.info("There are " + count + " daily emails has been sent about challenge summary");
        }
    }
}
