package com.techlooper.controller;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.model.JobSearchCriteria;
import com.techlooper.model.JobSearchResponse;
import com.techlooper.service.JobAggregatorService;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import static com.techlooper.model.JobAlertEmailResultEnum.EMAIL_SENT;
import static com.techlooper.model.JobAlertEmailResultEnum.JOB_NOT_FOUND;
import static com.techlooper.model.JobAlertEmailResultEnum.SERVER_ERROR;

/**
 * Job alert registration controller
 *
 * @author Khoa Nguyen
 * @version v0.0-beta9.Release39, 09/09/2015
 */
@Controller
public class JobAlertController {

    private final static Logger LOGGER = LoggerFactory.getLogger(JobAlertController.class);

    @Resource
    private JobAggregatorService jobAggregatorService;

    @Resource
    private Mapper dozerMapper;

    /**
     * This method is used to register job alert for user
     *
     * @param jobAlertRegistration
     * @param response
     * @return jobAlertRegistrationEntity
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "jobAlert/register", method = RequestMethod.POST)
    public JobAlertRegistrationEntity registerJobAlert(@RequestBody JobAlertRegistration jobAlertRegistration,
                                                       HttpServletResponse response) throws Exception {
        //Check number of registration for on user has been exceeded the limit or not
        boolean isOverLimit = jobAggregatorService.exceedJobAlertRegistrationLimit(jobAlertRegistration.getEmail());
        if (isOverLimit) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }

        //Register a new job alert record for this user
        JobAlertRegistrationEntity jobAlertRegistrationEntity = jobAggregatorService.registerJobAlert(jobAlertRegistration);

        //Send list of jobs that matches user's criteria immediately after user submitted registration form
        JobSearchCriteria criteria = dozerMapper.map(jobAlertRegistrationEntity, JobSearchCriteria.class);
        JobSearchResponse jobSearchResponse = jobAggregatorService.findJob(criteria);
        if (jobSearchResponse.getTotalJob() > 0) {
            try {
                jobAggregatorService.sendEmail(jobAlertRegistrationEntity, jobSearchResponse);
                jobAggregatorService.updateSendEmailResultCode(jobAlertRegistrationEntity, EMAIL_SENT);
            } catch (Exception ex) {
                jobAggregatorService.updateSendEmailResultCode(jobAlertRegistrationEntity, SERVER_ERROR);
                LOGGER.error(ex.getMessage(), ex);
            }
        } else {
            jobAggregatorService.updateSendEmailResultCode(jobAlertRegistrationEntity, JOB_NOT_FOUND);
        }

        return jobAlertRegistrationEntity;
    }

}
