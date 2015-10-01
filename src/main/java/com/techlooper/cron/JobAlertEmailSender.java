package com.techlooper.cron;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.model.JobAlertPeriodEnum;
import com.techlooper.model.JobSearchCriteria;
import com.techlooper.model.JobSearchResponse;
import com.techlooper.service.JobAggregatorService;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.techlooper.model.JobAlertEmailResultEnum.EMAIL_SENT;
import static com.techlooper.model.JobAlertEmailResultEnum.JOB_NOT_FOUND;

/**
 * This is cron job class that automatically sends job alert email to user periodically
 *
 * @author Khoa Nguyen
 * @version v0.0-beta9.Release39, 09/09/2015
 */
@Service
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
    public void sendJobAlertEmail() throws Exception {
        if (enableJobAlert) {
            List<JobAlertRegistrationEntity> jobAlertRegistrationEntities =
                    jobAggregatorService.findJobAlertRegistration(JobAlertPeriodEnum.WEEKLY);

            if (!jobAlertRegistrationEntities.isEmpty()) {
                int count = 0;
                for (JobAlertRegistrationEntity jobAlertRegistrationEntity : jobAlertRegistrationEntities) {
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
                LOGGER.info("There are " + count + " job alert emails has been sent");
            }
        }
    }
}
