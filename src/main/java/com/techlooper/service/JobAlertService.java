package com.techlooper.service;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.model.JobListingCriteria;
import com.techlooper.model.JobResponse;

import javax.mail.internet.AddressException;
import java.util.List;

public interface JobAlertService {

    List<ScrapeJobEntity> searchJob(JobAlertRegistrationEntity jobAlertRegistrationEntity);

    JobAlertRegistrationEntity registerJobAlert(JobAlertRegistration jobAlertRegistration) throws Exception;

    List<JobAlertRegistrationEntity> searchJobAlertRegistration(int period) throws Exception;

    void sendEmail(JobAlertRegistrationEntity jobAlertRegistrationEntity, List<ScrapeJobEntity> scrapeJobEntities) throws Exception;

    List<JobResponse> listJob(JobListingCriteria criteria);

    Long countJob(JobListingCriteria criteria);
}
