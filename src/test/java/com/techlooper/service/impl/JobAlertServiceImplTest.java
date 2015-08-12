package com.techlooper.service.impl;

import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.config.JobAlertServiceConfigurationTest;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertCriteria;
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
        JobAlertCriteria jobAlertCriteria = new JobAlertCriteria();
        jobAlertCriteria.setEmail("ndkhoa.is@gmail.com");
        jobAlertCriteria.setKeyword("Java");
        jobAlertCriteria.setLocation("Ho Chi Minh");
        List<ScrapeJobEntity> jobs = jobAlertService.searchJob(jobAlertCriteria);
        Assert.assertTrue(jobs.size() > 0);
    }
}