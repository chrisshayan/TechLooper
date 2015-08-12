package com.techlooper.controller;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.service.JobAlertService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class JobAlertController {

    @Value("${jobAlert.period}")
    private int CONFIGURED_JOB_ALERT_PERIOD;

    @Resource
    private JobAlertService jobAlertService;

    @ResponseBody
    @RequestMapping(value = "jobAlert/register", method = RequestMethod.POST)
    public JobAlertRegistrationEntity getCompany(@RequestBody JobAlertRegistration jobAlertRegistration) throws Exception {
        return jobAlertService.registerJobAlert(jobAlertRegistration);
    }

    @Scheduled(cron = "${scheduled.cron.jobAlert}")
    public void sendJobAlertEmail() throws Exception {
        List<JobAlertRegistrationEntity> jobAlertRegistrationEntities =
                jobAlertService.searchJobAlertRegistration(CONFIGURED_JOB_ALERT_PERIOD);

        if (!jobAlertRegistrationEntities.isEmpty()) {
            for (JobAlertRegistrationEntity jobAlertRegistrationEntity : jobAlertRegistrationEntities) {
                List<ScrapeJobEntity> scrapeJobEntities = jobAlertService.searchJob(jobAlertRegistrationEntity);
                // TODO : send email to <jobAlertRegistrationEntity.getEmail()> with list of scrapeJobEntities
            }
        }
    }

}
