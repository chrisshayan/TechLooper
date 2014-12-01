package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.model.HistogramEnum;
import com.techlooper.model.SkillStatisticResponse;
import com.techlooper.model.TechnicalTerm;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Created by chrisshayan on 7/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchConfiguration.class})
public class VietnamWorksJobStatisticServiceTest {

    private JobStatisticService jobStatisticService;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    @Value("${elasticsearch.index.name}")
    private String elasticSearchIndexName;

    @Before
    public void before() {
        jobStatisticService = new VietnamWorksJobStatisticService();
        ReflectionTestUtils.setField(jobStatisticService, "elasticsearchTemplate", elasticsearchTemplate);
        ReflectionTestUtils.setField(jobStatisticService, "jobQueryBuilder", jobQueryBuilder);
        ReflectionTestUtils.setField(jobStatisticService, "elasticSearchIndexName", elasticSearchIndexName);
    }

    @Test
    public void testCountJobsBySkill() {
        // TODO : Because we've changed the logic how to load JSON file. So we should re-implement this test case later
    }
}