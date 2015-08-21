package com.techlooper.controller;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.service.JobAlertService;
import com.techlooper.util.DateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Controller
public class JobAlertController {

    @Value("${jobAlert.period}")
    private int CONFIGURED_JOB_ALERT_PERIOD;

    @Value("${jobAlert.enable}")
    private Boolean enableJobAlert;

    @Resource
    private JobAlertService jobAlertService;

    @ResponseBody
    @RequestMapping(value = "jobAlert/register", method = RequestMethod.POST)
    public JobAlertRegistrationEntity registerJobAlert(@RequestBody JobAlertRegistration jobAlertRegistration,
                                                       HttpServletResponse response) throws Exception {
        boolean isOverLimit = jobAlertService.checkIfUserExceedRegistrationLimit(jobAlertRegistration.getEmail());
        if (isOverLimit) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }

        JobAlertRegistrationEntity jobAlertRegistrationEntity = jobAlertService.registerJobAlert(jobAlertRegistration);
        Long numberOfJobs = jobAlertService.countJob(jobAlertRegistrationEntity);
        if (numberOfJobs > 0) {
            List<ScrapeJobEntity> scrapeJobEntities = jobAlertService.searchJob(jobAlertRegistrationEntity);
            jobAlertService.sendEmail(numberOfJobs, jobAlertRegistrationEntity, scrapeJobEntities);
        }

        return jobAlertRegistrationEntity;
    }

    @Scheduled(cron = "${scheduled.cron.jobAlert}")
    public void sendJobAlertEmail() throws Exception {
        if (enableJobAlert) {
            List<JobAlertRegistrationEntity> jobAlertRegistrationEntities =
                    jobAlertService.searchJobAlertRegistration(CONFIGURED_JOB_ALERT_PERIOD);

            if (!jobAlertRegistrationEntities.isEmpty()) {
                for (JobAlertRegistrationEntity jobAlertRegistrationEntity : jobAlertRegistrationEntities) {
                    boolean isAlreadySentToday = checkIfEmailAlreadySentToday(jobAlertRegistrationEntity.getLastEmailSentDateTime());

                    if (!isAlreadySentToday) {
                        Long numberOfJobs = jobAlertService.countJob(jobAlertRegistrationEntity);
                        if (numberOfJobs > 0) {
                            List<ScrapeJobEntity> scrapeJobEntities = jobAlertService.searchJob(jobAlertRegistrationEntity);
                            if (!scrapeJobEntities.isEmpty()) {
                                jobAlertService.sendEmail(numberOfJobs, jobAlertRegistrationEntity, scrapeJobEntities);
                            }
                        }
                    }
                }
            }
        }
    }

    @RequestMapping(value = "jobAlert/redirect", method = RequestMethod.GET)
    public void redirect(@RequestParam(required = true) String targetUrl, HttpServletResponse response) throws IOException {
        response.sendRedirect(targetUrl);
    }

    private boolean checkIfEmailAlreadySentToday(String lastEmailSentDateTime) {
        if (lastEmailSentDateTime == null) {
            return false;
        }

        try {
            Date lastSentDate = DateTimeUtils.parseString2Date(lastEmailSentDateTime, "dd/MM/yyyy HH:mm");
            Date currentDate = new Date();
            int diffDays = Days.daysBetween(new DateTime(lastSentDate), new DateTime(currentDate)).getDays();
            return diffDays == 0 ? true : false;
        } catch (ParseException e) {
            return false;
        }
    }

}
