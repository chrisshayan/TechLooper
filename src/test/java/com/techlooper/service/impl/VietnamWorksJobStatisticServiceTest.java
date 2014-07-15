package com.techlooper.service.impl;

import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobStatisticService;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertThat;

/**
 * Created by chrisshayan on 7/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-VietnamWorksJobStatisticService-Test.xml")
public class VietnamWorksJobStatisticServiceTest {

    @Autowired
    JobStatisticService jobStatisticService;

    @Test
    public void countPhpJobs() {
        assertThat(jobStatisticService.countPhpJobs(), IsNot.not(IsNull.nullValue()));
    }

    @Test
    public void countJavaJobs() {
        assertThat(jobStatisticService.countJavaJobs(), IsNot.not(IsNull.nullValue()));
    }

    @Test
    public void countDotNetJobs() {
        assertThat(jobStatisticService.countDotNetJobs(), IsNot.not(IsNull.nullValue()));
    }

    @Test
    public void count() {
        assertThat(jobStatisticService.count(TechnicalTermEnum.JAVA), Is.is(jobStatisticService.countJavaJobs()));
    }
}
