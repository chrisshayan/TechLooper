package com.techlooper.cron;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.service.ChallengeRegistrantService;
import com.techlooper.service.ChallengeService;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.techlooper.model.ChallengePhaseEnum.*;
import static com.techlooper.util.DateTimeUtils.currentDate;
import static com.techlooper.util.DateTimeUtils.daysBetween;

public class ChallengeSubmissionNotifier {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChallengeSubmissionNotifier.class);

    @Resource
    private ChallengeService challengeService;

    @Resource
    private ChallengeRegistrantService challengeRegistrantService;

    @Resource
    private Mapper dozerMapper;

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
                            registrant.getDisqualified() == false);
                    Predicate<ChallengeRegistrantEntity> beforeTwoDays = registrant -> isBeforeTwoDays(challengeEntity, challengePhase);
                    registrants = registrants.stream().filter(isFollowingUp).filter(notDisqualified)
                            .filter(beforeTwoDays).collect(Collectors.toList());

                    for (ChallengeRegistrantEntity registrantEntity : registrants) {
                        try {
                            challengeService.sendEmailNotifyRegistrantAboutChallengeTimeline(
                                    challengeEntity, registrantEntity, challengePhase);
                            count++;
                        } catch (Exception ex) {
                            LOGGER.error(ex.getMessage(), ex);
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
