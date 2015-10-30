package com.techlooper.cron;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.model.JobAlertPeriodEnum;
import com.techlooper.model.JobSearchCriteria;
import com.techlooper.model.JobSearchResponse;
import com.techlooper.service.JobAggregatorService;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.techlooper.model.JobAlertEmailResultEnum.EMAIL_SENT;
import static com.techlooper.model.JobAlertEmailResultEnum.JOB_NOT_FOUND;
import static com.techlooper.util.DateTimeUtils.*;

/**
 * This is cron job class that automatically sends job alert email to user periodically
 *
 * @author Khoa Nguyen
 * @version v0.0-beta9.Release39, 09/09/2015
 */
public class JobAlertEmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(JobAlertEmailSender.class);

    @Value("${jobAlert.enable}")
    private Boolean enableJobAlert;

    @Resource
    private JobAggregatorService jobAggregatorService;

    @Resource
    private Mapper dozerMapper;

    /**
     * Send job alert email to registered user and update result code into ElasticSearch
     * EMAIL_SENT : 200, send email successfully
     * JOB_NOT_FOUND : 400, there is no job matches user's criteria
     *
     * @throws Exception
     */
    @Scheduled(cron = "${scheduled.cron.jobAlert}")
    public synchronized void sendJobAlertEmail() throws Exception {
        if (enableJobAlert) {
            List<JobAlertRegistrationEntity> jobAlertRegistrationEntities =
                    jobAggregatorService.findJobAlertRegistration(JobAlertPeriodEnum.WEEKLY);

            if (!jobAlertRegistrationEntities.isEmpty()) {
                int count = 0;
                Thread.sleep(getRandomNumberInRange(300000, 600000));
                for (JobAlertRegistrationEntity jobAlertRegistrationEntity : jobAlertRegistrationEntities) {
                    jobAlertRegistrationEntity = jobAggregatorService.getJobAlertRegistrationById(
                            jobAlertRegistrationEntity.getJobAlertRegistrationId());
                    if (StringUtils.isEmpty(jobAlertRegistrationEntity.getLastEmailSentDateTime())) {
                        jobAlertRegistrationEntity.setLastEmailSentDateTime(yesterdayDate(BASIC_DATE_TIME_PATTERN));
                    }

                    Date lastSentDate = string2Date(jobAlertRegistrationEntity.getLastEmailSentDateTime(), BASIC_DATE_TIME_PATTERN);
                    Date currentDate = new Date();

                    if (daysBetween(lastSentDate, currentDate) > 0) {
                        JobSearchCriteria criteria = dozerMapper.map(jobAlertRegistrationEntity, JobSearchCriteria.class);
                        JobSearchResponse jobSearchResponse = jobAggregatorService.findJob(criteria);
                        if (jobSearchResponse.getTotalJob() > 0) {
                            jobAggregatorService.sendEmail(jobAlertRegistrationEntity, jobSearchResponse);
                            jobAggregatorService.updateSendEmailResultCode(jobAlertRegistrationEntity, EMAIL_SENT);
                            count++;
                        } else {
                            jobAggregatorService.updateSendEmailResultCode(jobAlertRegistrationEntity, JOB_NOT_FOUND);
                        }
                    }
                }
                LOGGER.info("There are " + count + " job alert emails has been sent");
            }
        }
    }

    private static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
    }
}
