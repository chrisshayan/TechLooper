package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.entity.Company;
import com.techlooper.model.*;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.CompanyService;
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
import static org.junit.Assert.assertThat;
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

    @Resource
    private CompanyService companyService;

    @Resource
    private JsonConfigRepository jsonConfigRepository;

    @Before
    public void before() {
        jobStatisticService = new VietnamWorksJobStatisticService();
        ReflectionTestUtils.setField(jobStatisticService, "elasticsearchTemplate", elasticsearchTemplate);
        ReflectionTestUtils.setField(jobStatisticService, "jobQueryBuilder", jobQueryBuilder);
        ReflectionTestUtils.setField(jobStatisticService, "elasticSearchIndexName", elasticSearchIndexName);
        ReflectionTestUtils.setField(jobStatisticService, "companyService", companyService);
        ReflectionTestUtils.setField(jobStatisticService, "jsonConfigRepository", jsonConfigRepository);
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
    public void testGenerateTermStatisticWithDefaultSkill() throws Exception {
        TermStatisticRequest termStatisticRequest = new TermStatisticRequest();
        termStatisticRequest.setTerm("JAVA");
        termStatisticRequest.setJobLevelIds(Arrays.asList(1, 5));
        TermStatisticResponse termStatisticResponse =
                jobStatisticService.generateTermStatistic(termStatisticRequest, HistogramEnum.ONE_YEAR);
        assertTrue(termStatisticResponse.getAverageSalaryMin() > 250);
        assertTrue(termStatisticResponse.getAverageSalaryMax() > 250);

        List<SkillStatistic> skillStatistics = termStatisticResponse.getSkills();
        List<Company> companies = termStatisticResponse.getCompanies();

        assertTrue(termStatisticResponse.getTotalJob() > 0);

        // Maximum number of top hiring companies
        assertTrue(companies.size() > 0 && companies.size() <= 5);

        // Maximum number of skills can be added
        assertTrue(skillStatistics.size() > 0 && skillStatistics.size() <= 5);

        // One skill should be analyzed by 12 months per year
        assertTrue(skillStatistics.get(0).getHistograms().get(0).getValues().size() >= 12);
    }

    @Test
    public void testGenerateTermStatisticWithCustomAddedSkill() throws Exception {
        TermStatisticRequest termStatisticRequest = new TermStatisticRequest();
        termStatisticRequest.setTerm("JAVA");
        termStatisticRequest.setJobLevelIds(Arrays.asList(5, 6));
        termStatisticRequest.setSkills(Arrays.asList("Hadoop", "Scala"));
        TermStatisticResponse termStatisticResponse =
                jobStatisticService.generateTermStatistic(termStatisticRequest, HistogramEnum.ONE_YEAR);
        assertTrue(termStatisticResponse.getAverageSalaryMin() > 250);
        assertTrue(termStatisticResponse.getAverageSalaryMax() > 250);

        List<SkillStatistic> skillStatistics = termStatisticResponse.getSkills();
        List<Company> companies = termStatisticResponse.getCompanies();

        assertTrue(termStatisticResponse.getTotalJob() > 0);

        // Maximum number of top hiring companies
        assertTrue(companies.size() > 0 && companies.size() <= 5);

        // Maximum number of skills can be added
        assertTrue(skillStatistics.size() > 0 && skillStatistics.size() <= 5);

        // One skill should be analyzed by 12 months per year
        assertTrue(skillStatistics.get(0).getHistograms().get(0).getValues().size() > 0);
    }

    @Test
    public void testGetTop5DemandedSkillsByJobTitle() throws Exception {
        List<SkillStatistic> skillStatistics =
                jobStatisticService.getTopDemandedSkillsByJobTitle("Java Developer", Arrays.asList(35L), 5, 5);
        assertTrue(skillStatistics.size() > 0);
    }
}