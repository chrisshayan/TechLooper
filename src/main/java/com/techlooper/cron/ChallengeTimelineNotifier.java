package com.techlooper.cron;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantDto;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.EmailSentResultEnum;
import com.techlooper.service.ChallengeService;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.techlooper.util.DateTimeUtils.*;

@Service
public class ChallengeTimelineNotifier {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeTimelineNotifier.class);

    @Resource
    private ChallengeService challengeService;

    @Resource
    private Mapper dozerMapper;

    @Value("${jobAlert.enable}")
    private Boolean enableJobAlert;

    @Scheduled(cron = "${scheduled.cron.notifyChallengeTimeline}")
    public synchronized void notifyRegistrantAboutChallengeTimeline() throws Exception {
        if (enableJobAlert) {
            List<ChallengePhaseEnum> challengePhases = Arrays.asList(ChallengePhaseEnum.REGISTRATION, ChallengePhaseEnum.IN_PROGRESS);

            int count = 0;
            for (ChallengePhaseEnum challengePhase : challengePhases) {
                List<ChallengeEntity> challengeEntities = challengeService.listChallengesByPhase(challengePhase);

                for (ChallengeEntity challengeEntity : challengeEntities) {
                    Set<ChallengeRegistrantDto> challengeRegistrants = challengeService.findRegistrantsByOwner(
                            challengeEntity.getAuthorEmail(), challengeEntity.getChallengeId());
                    
                    for (ChallengeRegistrantDto challengeRegistrant : challengeRegistrants) {
                        Date lastSentDate = string2Date(challengeRegistrant.getLastEmailSentDateTime(), BASIC_DATE_TIME_PATTERN);
                        Date currentDate = new Date();

                        if (daysBetween(lastSentDate, currentDate) > 0) {
                            ChallengeRegistrantEntity challengeRegistrantEntity = dozerMapper.map(challengeRegistrant, ChallengeRegistrantEntity.class);
                            try {
                                if (StringUtils.isNotEmpty(challengeRegistrantEntity.getRegistrantEmail())) {
                                    challengeService.sendEmailNotifyRegistrantAboutChallengeTimeline(
                                            challengeEntity, challengeRegistrantEntity, challengePhase);
                                    challengeService.updateSendEmailToContestantResultCode(challengeRegistrantEntity, EmailSentResultEnum.OK);
                                    count++;
                                }
                            } catch (Exception ex) {
                                LOGGER.error(ex.getMessage(), ex);
                                challengeService.updateSendEmailToContestantResultCode(challengeRegistrantEntity, EmailSentResultEnum.ERROR);
                            }
                        }
                    }
                }
            }
            LOGGER.info("There are " + count + " emails has been sent to notify challenge timeline");
        }
    }

}
