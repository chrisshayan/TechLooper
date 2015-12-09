package com.techlooper.service;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.model.*;

import java.util.List;

public interface JobAggregatorService {

    JobSearchResponse findJob(JobSearchCriteria criteria);

    void updateJobExpiration(JobSearchCriteria criteria);

    JobAlertRegistrationEntity registerJobAlert(JobAlertRegistration jobAlertRegistration) throws Exception;

    JobSearchCriteria findJobAlertCriteriaById(Long jobAlertRegistrationId);

    List<JobAlertRegistrationEntity> findJobAlertRegistration(JobAlertPeriodEnum period) throws Exception;

    void sendEmail(JobAlertRegistrationEntity jobAlertRegistrationEntity, JobSearchResponse jobSearchResponse) throws Exception;

    boolean exceedJobAlertRegistrationLimit(String email);

    void updateSendEmailResultCode(JobAlertRegistrationEntity jobAlertRegistrationEntity, JobAlertEmailResultEnum result);

    int importVietnamworksJob(JobTypeEnum jobType);

    JobAlertRegistrationEntity getJobAlertRegistrationById(Long id);

    void createVnwJobAlert(VnwJobAlertRequest vnwJobAlertRequest);

}
