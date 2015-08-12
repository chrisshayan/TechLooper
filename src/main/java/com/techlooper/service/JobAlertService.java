package com.techlooper.service;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertRegistration;

import java.util.List;

public interface JobAlertService {

    List<ScrapeJobEntity> searchJob(JobAlertRegistration jobAlertRegistration);

    JobAlertRegistrationEntity registerJobAlert(JobAlertRegistration jobAlertRegistration);

    List<JobAlertRegistrationEntity> searchJobAlertRegistration(int period);

}
