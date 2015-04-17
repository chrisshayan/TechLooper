package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.model.HistogramEnum;
import com.techlooper.model.SkillStatisticResponse;
import com.techlooper.model.TechnicalTerm;
import com.techlooper.model.TermStatisticRequest;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by chrisshayan on 7/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchConfiguration.class, ElasticsearchUserImportConfiguration.class})
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
//        assertNotEquals(0, skillStatisticResponse.getCount().longValue());
//        assertNotEquals(0, skillStatisticResponse.getTotalTechnicalJobs().longValue());
//        assertNotEquals("", skillStatisticResponse.getLabel());
//        assertNotEquals("", skillStatisticResponse.getLogoUrl());
//        assertNotEquals("", skillStatisticResponse.getWebSite());
//        assertNotEquals(0, skillStatisticResponse.getSkills().size());
//        assertNotEquals(0, skillStatisticResponse.getUsefulLinks().size());
    }

    @Test
    public void testCountJobsBySkillWithinPeriod() throws Exception {
        Long numberOfJavaJobs = jobStatisticService.countJobsBySkillWithinPeriod("Java", HistogramEnum.TWO_QUARTERS);
        assertTrue(numberOfJavaJobs > 0);
    }

    @Test
    public void testCountTotalITJobsWithinPeriod() throws Exception {
        Long totalITJobs = jobStatisticService.countTotalITJobsWithinPeriod(HistogramEnum.TWO_QUARTERS);
        assertTrue(totalITJobs > 0);
    }

    @Test
    public void testGenerateTermStatistic() throws Exception {
        TermStatisticRequest termStatisticRequest = new TermStatisticRequest();
        termStatisticRequest.setTerm("JAVA");
        termStatisticRequest.setJobLevelId(5);
        termStatisticRequest.setSkills(Arrays.asList("Spring", "Maven"));
        jobStatisticService.generateTermStatistic(termStatisticRequest, HistogramEnum.ONE_YEAR);
        System.out.print("Test Done");
    }
}