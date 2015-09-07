package com.techlooper.service.impl;

import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.config.JobAlertServiceConfigurationTest;
import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.model.JobListingCriteria;
import com.techlooper.model.JobResponse;
import com.techlooper.model.Language;
import com.techlooper.repository.userimport.ScrapeJobRepository;
import com.techlooper.service.JobAggregatorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchUserImportConfiguration.class, JobAlertServiceConfigurationTest.class})
public class JobAggregatorServiceImplTest {

    @Resource
    private JobAggregatorService jobAggregatorService;

    @Resource
    private Set<String> topPriorityJobIds;

    @Resource
    private ScrapeJobRepository scrapeJobRepository;

    @Test
    public void testSearchJob() throws Exception {
        JobAlertRegistrationEntity jobAlertRegistration = new JobAlertRegistrationEntity();
        jobAlertRegistration.setEmail("ndkhoa.is@gmail.com");
        jobAlertRegistration.setKeyword("Java");
        jobAlertRegistration.setLocation("Ho Chi Minh");
        List<ScrapeJobEntity> jobs = jobAggregatorService.searchJob(jobAlertRegistration);
        Assert.assertTrue(jobs.size() > 0);
    }

    @Test
    public void testRegisterJobAlert() throws Exception {
        JobAlertRegistration jobAlertRegistration = new JobAlertRegistration();
        jobAlertRegistration.setEmail("ndkhoa.is@gmail.com");
        jobAlertRegistration.setKeyword("Java");
        jobAlertRegistration.setLocation("Ho Chi Minh");
        jobAlertRegistration.setLang(Language.en);
        JobAlertRegistrationEntity jobAlertRegistrationEntity = jobAggregatorService.registerJobAlert(jobAlertRegistration);
        Assert.assertNotNull(jobAlertRegistrationEntity);
        Assert.assertNotNull(jobAlertRegistrationEntity.getJobAlertRegistrationId());
    }

    @Test
    public void testListJob() throws Exception {
        JobListingCriteria criteria = new JobListingCriteria(1);
        criteria.setKeyword("Java Developer");
        criteria.setLocation("");
        criteria.setPage(0);
        List<JobResponse> jobs = jobAggregatorService.listJob(criteria);
        Assert.assertFalse(jobs.isEmpty());
    }

    @Test
    public void testListJobEmptyResult() throws Exception {
        JobListingCriteria criteria = new JobListingCriteria(1);
        criteria.setKeyword("ABC.XYZ");
        criteria.setLocation("");
        criteria.setPage(0);
        List<JobResponse> jobs = jobAggregatorService.listJob(criteria);
        Assert.assertTrue(jobs.isEmpty());
    }

    @Test
    public void updateTopPriorityJobManually() throws Exception {
        for(String jobId : topPriorityJobIds) {
            ScrapeJobEntity jobEntity = scrapeJobRepository.findOne(jobId);
            if (jobEntity != null) {
                jobEntity.setTopPriority(Boolean.TRUE);
                scrapeJobRepository.save(jobEntity);
            }
        }
    }
}