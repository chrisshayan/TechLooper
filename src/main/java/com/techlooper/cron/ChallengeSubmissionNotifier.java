package com.techlooper.cron;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.EmailSentResultEnum;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.techlooper.model.ChallengePhaseEnum.*;
import static com.techlooper.util.DateTimeUtils.*;

public class ChallengeSubmissionNotifier {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeSubmissionNotifier.class);

    @Resource
    private ChallengeService challengeService;

    @Resource
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Value("${jobAlert.enable}")
    private Boolean enableJobAlert;

    @Scheduled(cron = "${scheduled.cron.notifyChallengeSubmission}")
    public synchronized void notifyRegistrantAboutChallengeTimeline() throws Exception {
        if (enableJobAlert) {
            List<ChallengePhaseEnum> challengePhases = Arrays.asList(IDEA, UIUX, PROTOTYPE, FINAL);

            int count = 0;
            for (ChallengePhaseEnum challengePhase : challengePhases) {
                List<ChallengeEntity> challengeEntities = challengeService.listChallengesByPhase(challengePhase);

                for (ChallengeEntity challengeEntity : challengeEntities) {
                    List<ChallengeRegistrantEntity> registrants = challengeRegistrantService.findRegistrantsByChallengeId(
                            challengeEntity.getChallengeId());
                    Predicate<ChallengeRegistrantEntity> isFollowingUp = registrant -> (registrant.getActivePhase() != null &&
                            registrant.getActivePhase() == challengePhase);
                    Predicate<ChallengeRegistrantEntity> notDisqualified = registrant -> (registrant.getDisqualified() == null ||
                            !registrant.getDisqualified());
                    Predicate<ChallengeRegistrantEntity> beforeTwoDays = registrant -> isBeforeTwoDays(challengeEntity, challengePhase);
                    registrants = registrants.stream().filter(isFollowingUp).filter(notDisqualified)
                            .filter(beforeTwoDays).collect(Collectors.toList());

                    Thread.sleep(DataUtils.getRandomNumberInRange(300000, 600000));
                    for (ChallengeRegistrantEntity registrantEntity : registrants) {
                        registrantEntity = challengeRegistrantRepository.findOne(registrantEntity.getRegistrantId());

                        if (StringUtils.isEmpty(registrantEntity.getLastEmailSentDateTime())) {
                            registrantEntity.setLastEmailSentDateTime(yesterdayDate(BASIC_DATE_TIME_PATTERN));
                        }

                        Date lastSentDate = string2Date(registrantEntity.getLastEmailSentDateTime(), BASIC_DATE_TIME_PATTERN);
                        Date currentDate = new Date();
                        if (daysBetween(lastSentDate, currentDate) > 0) {
                            try {
                                challengeService.sendEmailNotifyRegistrantAboutChallengeTimeline(
                                        challengeEntity, registrantEntity, challengePhase, true);
                                challengeService.updateSendEmailToContestantResultCode(registrantEntity, EmailSentResultEnum.OK);
                                count++;
                            } catch (Exception ex) {
                                LOGGER.error(ex.getMessage(), ex);
                                challengeService.updateSendEmailToContestantResultCode(registrantEntity, EmailSentResultEnum.ERROR);
                            }
                        }
                    }
                }
            }
            LOGGER.info("There are " + count + " emails has been sent to notify registrants before challenge submission date");
        }
    }

    private boolean isBeforeTwoDays(ChallengeEntity challengeEntity, ChallengePhaseEnum challengePhase) {
        final int TIME_DISTANCE = 1;
        try {
            if (challengePhase == ChallengePhaseEnum.IDEA) {
                return daysBetween(currentDate(), challengeEntity.getIdeaSubmissionDateTime()) == TIME_DISTANCE;
            } else if (challengePhase == ChallengePhaseEnum.UIUX) {
                return daysBetween(currentDate(), challengeEntity.getUxSubmissionDateTime()) == TIME_DISTANCE;
            } else if (challengePhase == ChallengePhaseEnum.PROTOTYPE) {
                return daysBetween(currentDate(), challengeEntity.getPrototypeSubmissionDateTime()) == TIME_DISTANCE;
            } else if (challengePhase == ChallengePhaseEnum.IN_PROGRESS) {
                return daysBetween(currentDate(), challengeEntity.getSubmissionDateTime()) == TIME_DISTANCE;
            } else if (challengePhase == ChallengePhaseEnum.FINAL) {
                return daysBetween(currentDate(), challengeEntity.getSubmissionDateTime()) == TIME_DISTANCE;
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return false;
    }
}
