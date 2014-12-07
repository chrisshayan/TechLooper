package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.model.HistogramEnum;
import com.techlooper.model.SkillStatisticResponse;
import com.techlooper.model.TechnicalTerm;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobStatisticService;
import com.techlooper.util.JsonUtils;
import org.apache.commons.io.IOUtils;
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
import java.util.Optional;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by chrisshayan on 7/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchConfiguration.class})
public class VietnamWorksJobStatisticServiceITCase {

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
    public void testCountJobsBySkill() throws Exception {
        String jsonSkill = IOUtils.toString(getClass().getResourceAsStream("/expect/skill.json"), "UTF-8");
        Optional<List<TechnicalTerm>> termsOptional = JsonUtils.toList(jsonSkill, TechnicalTerm.class);
        List<TechnicalTerm> terms = termsOptional.get();
        TechnicalTerm javaTerm = terms.get(0);

        SkillStatisticResponse skillStatisticResponse = jobStatisticService.countJobsBySkill(
                javaTerm, new HistogramEnum[]{HistogramEnum.TWO_WEEKS, HistogramEnum.ONE_WEEK});

        assertNotNull(skillStatisticResponse);
        assertNotEquals(0, skillStatisticResponse.getCount().longValue());
        assertNotEquals(0, skillStatisticResponse.getTotalTechnicalJobs().longValue());
        assertNotEquals("", skillStatisticResponse.getLabel());
        assertNotEquals("", skillStatisticResponse.getLogoUrl());
        assertNotEquals("", skillStatisticResponse.getWebSite());
        assertNotEquals(0, skillStatisticResponse.getSkills().size());
        assertNotEquals(0, skillStatisticResponse.getUsefulLinks().size());
    }
}