package com.techlooper.service;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.model.JobListingCriteria;
import com.techlooper.model.JobResponse;
import com.techlooper.model.VNWJobSearchResponseDataItem;

import javax.mail.internet.AddressException;
import java.util.List;
import java.util.Set;

public interface JobAlertService {

    List<ScrapeJobEntity> searchJob(JobAlertRegistrationEntity jobAlertRegistrationEntity);

    Long countJob(JobAlertRegistrationEntity jobAlertRegistrationEntity);

    JobAlertRegistrationEntity registerJobAlert(JobAlertRegistration jobAlertRegistration) throws Exception;

    List<JobAlertRegistrationEntity> searchJobAlertRegistration(int period) throws Exception;

    void sendEmail(Long numberOfJobs, JobAlertRegistrationEntity jobAlertRegistrationEntity, List<ScrapeJobEntity> scrapeJobEntities) throws Exception;

    List<JobResponse> listJob(JobListingCriteria criteria);

    List<JobResponse> listNormalJob(JobListingCriteria criteria, int limit, int totalPage);

    Long countJob(JobListingCriteria criteria);

    boolean checkIfUserExceedRegistrationLimit(String email);

    void updateSendEmailResultCode(JobAlertRegistrationEntity jobAlertRegistrationEntity, Integer code);

    int importVietnamworksJob(int jobType);

}
