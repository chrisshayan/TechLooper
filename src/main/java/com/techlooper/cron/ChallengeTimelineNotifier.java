package com.techlooper.cron;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.EmailSentResultEnum;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.service.ChallengeEmailService;
import com.techlooper.service.ChallengeRegistrantService;
import com.techlooper.service.ChallengeService;
import com.techlooper.util.DataUtils;
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

public class ChallengeTimelineNotifier {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeTimelineNotifier.class);

    @Resource
    private ChallengeService challengeService;

    @Resource
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private ChallengeEmailService challengeEmailService;

    @Value("${jobAlert.enable}")
    private Boolean enableJobAlert;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Scheduled(cron = "${scheduled.cron.notifyChallengeTimeline}")
    public synchronized void notifyRegistrantAboutChallengeTimeline() throws Exception {
        if (enableJobAlert) {
            List<ChallengePhaseEnum> challengePhases = Arrays.asList(ChallengePhaseEnum.REGISTRATION, ChallengePhaseEnum.IN_PROGRESS);

            int count = 0;
            for (ChallengePhaseEnum challengePhase : challengePhases) {
                List<ChallengeEntity> challengeEntities = challengeService.findChallengeByPhase(challengePhase);

                for (ChallengeEntity challengeEntity : challengeEntities) {
                    Long challengeId = challengeEntity.getChallengeId();
                    List<ChallengeRegistrantEntity> challengeRegistrants = challengeRegistrantService.findRegistrantsByChallengeId(challengeId);

                    Thread.sleep(DataUtils.getRandomNumberInRange(300000, 600000));
                    for (ChallengeRegistrantEntity challengeRegistrant : challengeRegistrants) {
                        ChallengeRegistrantEntity challengeRegistrantEntity = challengeRegistrantRepository.findOne(
                                challengeRegistrant.getRegistrantId());
                        if (StringUtils.isEmpty(challengeRegistrantEntity.getLastEmailSentDateTime())) {
                            challengeRegistrantEntity.setLastEmailSentDateTime(yesterdayDate(BASIC_DATE_TIME_PATTERN));
                        }

                        Date lastSentDate = string2Date(challengeRegistrantEntity.getLastEmailSentDateTime(), BASIC_DATE_TIME_PATTERN);
                        Date currentDate = new Date();
                        if (daysBetween(lastSentDate, currentDate) > 0) {
                            try {
                                if (StringUtils.isNotEmpty(challengeRegistrantEntity.getRegistrantEmail())) {
                                    challengeEmailService.sendEmailNotifyRegistrantAboutChallengeTimeline(
                                            challengeEntity, challengeRegistrantEntity, challengePhase, false);
                                    challengeEmailService.updateSendEmailToContestantResultCode(challengeRegistrantEntity, EmailSentResultEnum.OK);
                                    count++;
                                }
                            } catch (Exception ex) {
                                LOGGER.error(ex.getMessage(), ex);
                                challengeEmailService.updateSendEmailToContestantResultCode(challengeRegistrantEntity, EmailSentResultEnum.ERROR);
                            }
                        }
                    }
                }
            }
            LOGGER.info("There are " + count + " emails has been sent to notify challenge timeline");
        }
    }

}
