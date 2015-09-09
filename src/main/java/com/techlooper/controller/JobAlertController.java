package com.techlooper.controller;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.model.JobSearchCriteria;
import com.techlooper.model.JobSearchResponse;
import com.techlooper.service.JobAggregatorService;
import com.techlooper.util.DateTimeUtils;
import org.dozer.Mapper;
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

import static com.techlooper.service.impl.JobAggregatorServiceImpl.JOB_ALERT_ALREADY_SENT_ON_TODAY;
import static com.techlooper.service.impl.JobAggregatorServiceImpl.JOB_ALERT_JOB_NOT_FOUND;

@Controller
public class JobAlertController {

    @Value("${jobAlert.period}")
    private int CONFIGURED_JOB_ALERT_PERIOD;

    @Value("${jobAlert.enable}")
    private Boolean enableJobAlert;

    @Resource
    private JobAggregatorService jobAggregatorService;

    @Resource
    private Mapper dozerMapper;

    @ResponseBody
    @RequestMapping(value = "jobAlert/register", method = RequestMethod.POST)
    public JobAlertRegistrationEntity registerJobAlert(@RequestBody JobAlertRegistration jobAlertRegistration,
                                                       HttpServletResponse response) throws Exception {
        boolean isOverLimit = jobAggregatorService.exceedJobAlertRegistrationLimit(jobAlertRegistration.getEmail());
        if (isOverLimit) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }

        JobAlertRegistrationEntity jobAlertRegistrationEntity = jobAggregatorService.registerJobAlert(jobAlertRegistration);

        JobSearchCriteria criteria = dozerMapper.map(jobAlertRegistrationEntity, JobSearchCriteria.class);
        JobSearchResponse jobSearchResponse = jobAggregatorService.findJob(criteria);
        Long totalJob = jobSearchResponse.getTotalJob();
        if (jobSearchResponse.getTotalJob() > 0) {
            jobAggregatorService.sendEmail(jobAlertRegistrationEntity, jobSearchResponse);
        }

        return jobAlertRegistrationEntity;
    }

    @Scheduled(cron = "${scheduled.cron.jobAlert}")
    public void sendJobAlertEmail() throws Exception {
        if (enableJobAlert) {
            List<JobAlertRegistrationEntity> jobAlertRegistrationEntities =
                    jobAggregatorService.findJobAlertRegistration(CONFIGURED_JOB_ALERT_PERIOD);

            if (!jobAlertRegistrationEntities.isEmpty()) {
                for (JobAlertRegistrationEntity jobAlertRegistrationEntity : jobAlertRegistrationEntities) {
                    boolean isAlreadySentToday = checkIfEmailAlreadySentToday(jobAlertRegistrationEntity.getLastEmailSentDateTime());

                    if (!isAlreadySentToday) {
                        JobSearchCriteria criteria = dozerMapper.map(jobAlertRegistrationEntity, JobSearchCriteria.class);
                        JobSearchResponse jobSearchResponse = jobAggregatorService.findJob(criteria);
                        Long numberOfJobs = jobSearchResponse.getTotalJob();
                        if (numberOfJobs > 0) {
                            jobAggregatorService.sendEmail(jobAlertRegistrationEntity, jobSearchResponse);
                        } else {
                            jobAggregatorService.updateSendEmailResultCode(jobAlertRegistrationEntity, JOB_ALERT_JOB_NOT_FOUND);
                        }
                    } else {
                        jobAggregatorService.updateSendEmailResultCode(jobAlertRegistrationEntity, JOB_ALERT_ALREADY_SENT_ON_TODAY);
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
            Date lastSentDate = DateTimeUtils.string2Date(lastEmailSentDateTime, "dd/MM/yyyy HH:mm");
            Date currentDate = new Date();
            int diffDays = Days.daysBetween(new DateTime(lastSentDate), new DateTime(currentDate)).getDays();
            return diffDays == 0 ? true : false;
        } catch (ParseException e) {
            return false;
        }
    }

}
