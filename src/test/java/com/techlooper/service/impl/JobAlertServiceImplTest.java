package com.techlooper.service.impl;

import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.config.JobAlertServiceConfigurationTest;
import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.service.JobAlertService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchUserImportConfiguration.class, JobAlertServiceConfigurationTest.class})
public class JobAlertServiceImplTest {

    @Resource
    private JobAlertService jobAlertService;

    @Test
    public void testSearchJob() throws Exception {
        JobAlertRegistrationEntity jobAlertRegistration = new JobAlertRegistrationEntity();
        jobAlertRegistration.setEmail("ndkhoa.is@gmail.com");
        jobAlertRegistration.setKeyword("Java");
        jobAlertRegistration.setLocation("Ho Chi Minh");
        List<ScrapeJobEntity> jobs = jobAlertService.searchJob(jobAlertRegistration);
        Assert.assertTrue(jobs.size() > 0);
    }

    @Test
    public void testRegisterJobAlert() throws Exception {
        JobAlertRegistration jobAlertRegistration = new JobAlertRegistration();
        jobAlertRegistration.setEmail("ndkhoa.is@gmail.com");
        jobAlertRegistration.setKeyword("C++");
        jobAlertRegistration.setLocation("Ho Chi Minh");
        JobAlertRegistrationEntity jobAlertRegistrationEntity = jobAlertService.registerJobAlert(jobAlertRegistration);
        Assert.assertNotNull(jobAlertRegistrationEntity);
        Assert.assertNotNull(jobAlertRegistrationEntity.getJobAlertRegistrationId());
    }
}