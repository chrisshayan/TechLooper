package com.techlooper.service;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.*;

import java.util.List;

public interface JobAggregatorService {

    List<ScrapeJobEntity> searchJob(JobAlertRegistrationEntity jobAlertRegistrationEntity);

    Long countJob(JobAlertRegistrationEntity jobAlertRegistrationEntity);

    JobAlertRegistrationEntity registerJobAlert(JobAlertRegistration jobAlertRegistration) throws Exception;

    List<JobAlertRegistrationEntity> searchJobAlertRegistration(int period) throws Exception;

    void sendEmail(Long numberOfJobs, JobAlertRegistrationEntity jobAlertRegistrationEntity, List<ScrapeJobEntity> scrapeJobEntities) throws Exception;

    JobSearchResponse listJob(JobSearchCriteria criteria);

    boolean checkIfUserExceedRegistrationLimit(String email);

    void updateSendEmailResultCode(JobAlertRegistrationEntity jobAlertRegistrationEntity, Integer code);

    int importVietnamworksJob(int jobType);

}
