package com.techlooper.service;

import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertCriteria;

import java.util.List;

public interface JobAlertService {

    List<ScrapeJobEntity> searchJob(JobAlertCriteria jobAlertCriteria);

}
