package com.techlooper.service.impl;

import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.config.UserEvaluationServiceConfigurationTest;
import com.techlooper.entity.JobEntity;
import com.techlooper.entity.PriceJobEntity;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.PriceJobSurvey;
import com.techlooper.model.SalaryReport;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.UserEvaluationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UserEvaluationServiceConfigurationTest.class, ElasticsearchConfiguration.class})
public class UserEvaluationServiceImplTest {

    @Resource
    private UserEvaluationService userEvaluationService;

    @Resource
    private JobSearchService jobSearchService;

    @Test
    public void testEvaluateJobOffer() throws Exception {
        SalaryReviewEntity salaryReviewEntity = new SalaryReviewEntity();
        salaryReviewEntity.setJobTitle("Java Developer");
        salaryReviewEntity.setJobLevelIds(Arrays.asList(5, 6));
        salaryReviewEntity.setJobCategories(Arrays.asList(35L));
        salaryReviewEntity.setNetSalary(1500);
        salaryReviewEntity.setSkills(Arrays.asList("Liferay", "Spring", "Hibernate"));
        userEvaluationService.reviewSalary(salaryReviewEntity);
        SalaryReport salaryReport = salaryReviewEntity.getSalaryReport();
        assertTrue(salaryReport.getNumberOfJobs() > 0);
        assertTrue(salaryReport.getNumberOfSurveys() > 0);
        assertTrue(salaryReport.getPercentRank() > 0);
        // delete data after test
        userEvaluationService.deleteSalaryReview(salaryReviewEntity);
    }

    @Test
    public void testEvaluateJobOfferDuplicatedJobTitle() throws Exception {
        SalaryReviewEntity salaryReviewEntity = new SalaryReviewEntity();
        salaryReviewEntity.setJobTitle("Sales Executive / Nhân viên bán hàng");
        salaryReviewEntity.setJobLevelIds(Arrays.asList(5, 6));
        salaryReviewEntity.setJobCategories(Arrays.asList(33L));
        salaryReviewEntity.setNetSalary(500);
        salaryReviewEntity.setSkills(Arrays.asList("Sales"));
        userEvaluationService.reviewSalary(salaryReviewEntity);
        SalaryReport salaryReport = salaryReviewEntity.getSalaryReport();
        assertTrue(salaryReport.getNumberOfJobs() > 0);
        assertTrue(salaryReport.getNumberOfSurveys() > 0);
        assertTrue(salaryReport.getPercentRank() > 0);
        // delete data after test
        userEvaluationService.deleteSalaryReview(salaryReviewEntity);
    }

    @Test
    public void testEvaluateJobOffer2() throws Exception {
        SalaryReviewEntity salaryReviewEntity = new SalaryReviewEntity();
        salaryReviewEntity.setJobTitle("Project Manager");
        salaryReviewEntity.setJobLevelIds(Arrays.asList(5, 6));
        salaryReviewEntity.setJobCategories(Arrays.asList(35L));
        salaryReviewEntity.setNetSalary(2200);
        userEvaluationService.reviewSalary(salaryReviewEntity);
        SalaryReport salaryReport = salaryReviewEntity.getSalaryReport();
        assertTrue(salaryReport.getPercentRank() > 0);
        // delete data after test
        userEvaluationService.deleteSalaryReview(salaryReviewEntity);
    }

    @Test
    public void testSalaryReviewRecommendationJobSkills() throws Exception {
        SalaryReviewEntity salaryReviewEntity = new SalaryReviewEntity();
        salaryReviewEntity.setJobTitle("Java Developer");
        salaryReviewEntity.setJobLevelIds(Arrays.asList(5, 6));
        salaryReviewEntity.setJobCategories(Arrays.asList(35L));
        salaryReviewEntity.setNetSalary(500);
        userEvaluationService.reviewSalary(salaryReviewEntity);
        SalaryReport salaryReport = salaryReviewEntity.getSalaryReport();
        assertTrue(salaryReport.getPercentRank() > 0);
        assertTrue(salaryReviewEntity.getTopPaidJobs().size() > 0);
        int numberOfSkills = salaryReviewEntity.getTopPaidJobs().get(0).getSkills().size();
        assertTrue(numberOfSkills > 0 && numberOfSkills <= 3);
        // delete data after test
        userEvaluationService.deleteSalaryReview(salaryReviewEntity);
    }

    @Test
    public void testEvaluateJobOfferWithoutJobs() throws Exception {
        SalaryReviewEntity salaryReviewEntity = new SalaryReviewEntity();
        salaryReviewEntity.setJobTitle("ABC XYZ");
        salaryReviewEntity.setJobLevelIds(Arrays.asList(5, 6));
        salaryReviewEntity.setJobCategories(Arrays.asList(35L));
        salaryReviewEntity.setNetSalary(2000);
        userEvaluationService.reviewSalary(salaryReviewEntity);
        SalaryReport salaryReport = salaryReviewEntity.getSalaryReport();
        assertTrue(salaryReport.getPercentRank().isNaN());
        assertTrue(salaryReport.getNumberOfJobs() + salaryReport.getNumberOfSurveys() < 10);
        // delete data after test
        userEvaluationService.deleteSalaryReview(salaryReviewEntity);
    }

    @Test
    public void testEvaluateJobOfferTotalJobLessThan10() throws Exception {
        SalaryReviewEntity salaryReviewEntity = new SalaryReviewEntity();
        salaryReviewEntity.setJobTitle("Product Manager");
        salaryReviewEntity.setJobLevelIds(Arrays.asList(5, 6));
        salaryReviewEntity.setJobCategories(Arrays.asList(35L));
        salaryReviewEntity.setSkills(Arrays.asList("Product Management", "UI/UX", "Product Strategy"));
        salaryReviewEntity.setNetSalary(2000);
        userEvaluationService.reviewSalary(salaryReviewEntity);
        SalaryReport salaryReport = salaryReviewEntity.getSalaryReport();
        assertTrue(salaryReport.getPercentRank() > 0);
        // delete data after test
        userEvaluationService.deleteSalaryReview(salaryReviewEntity);
    }

    @Test
    public void testEvaluateJobOfferTotalJobLessThan10WithEmptySkill() throws Exception {
        SalaryReviewEntity salaryReviewEntity = new SalaryReviewEntity();
        salaryReviewEntity.setJobTitle("Solution Architect");
        salaryReviewEntity.setJobLevelIds(Arrays.asList(5, 6));
        salaryReviewEntity.setJobCategories(Arrays.asList(35L));
        salaryReviewEntity.setSkills(Arrays.asList("Software Testing", "Software Architecture", "Software Construction"));
        salaryReviewEntity.setNetSalary(2000);
        userEvaluationService.reviewSalary(salaryReviewEntity);
        SalaryReport salaryReport = salaryReviewEntity.getSalaryReport();
        assertTrue(salaryReport.getPercentRank() > 0);
        // delete data after test
        userEvaluationService.deleteSalaryReview(salaryReviewEntity);
    }

    @Test
    public void testGetHigherSalaryJobs() {
        SalaryReviewEntity salaryReviewEntity = new SalaryReviewEntity();
        salaryReviewEntity.setJobTitle("Java Developer");
        salaryReviewEntity.setNetSalary(1000);
        salaryReviewEntity.setJobCategories(Arrays.asList(35L));
        List<JobEntity> jobEntities = jobSearchService.getHigherSalaryJobs(salaryReviewEntity);
        assertTrue(jobEntities.size() <= 3);
    }

    @Test
    public void testPriceJob() throws Exception {
        PriceJobEntity priceJobEntity = new PriceJobEntity();
        priceJobEntity.setJobTitle("Senior Java Developer");
        priceJobEntity.setLocationId(29);
        priceJobEntity.setJobLevelIds(Arrays.asList(5, 6));
        priceJobEntity.setJobCategories(Arrays.asList(35L, 55L, 57L));
        userEvaluationService.priceJob(priceJobEntity);
        assertTrue(priceJobEntity.getPriceJobReport().getTargetPay() > 0);
        assertTrue(priceJobEntity.getPriceJobReport().getAverageSalary() > 0);
        assertTrue(priceJobEntity.getPriceJobReport().getPriceJobSalaries().size() == 5);
    }

    @Test
    public void testPriceJobNoJob() throws Exception {
        PriceJobEntity priceJobEntity = new PriceJobEntity();
        priceJobEntity.setJobTitle("ABC XYZ");
        priceJobEntity.setLocationId(29);
        priceJobEntity.setJobLevelIds(Arrays.asList(5, 6));
        priceJobEntity.setJobCategories(Arrays.asList(35L, 55L, 57L));
        userEvaluationService.priceJob(priceJobEntity);
        assertTrue(priceJobEntity.getPriceJobReport().getTargetPay().isNaN());
        assertTrue(priceJobEntity.getPriceJobReport().getAverageSalary().isNaN());
    }

    @Test
    public void testPriceJobBySkills() throws Exception {
        PriceJobEntity priceJobEntity = new PriceJobEntity();
        priceJobEntity.setJobTitle("Technical Architect");
        priceJobEntity.setLocationId(29);
        priceJobEntity.setJobLevelIds(Arrays.asList(5, 6));
        priceJobEntity.setJobCategories(Arrays.asList(35L, 55L, 57L));
        priceJobEntity.setSkills(Arrays.asList("Java", "Spring", "Hibernate"));
        userEvaluationService.priceJob(priceJobEntity);
        assertTrue(priceJobEntity.getPriceJobReport().getTargetPay() > 0);
        assertTrue(priceJobEntity.getPriceJobReport().getAverageSalary() > 0);
        assertTrue(priceJobEntity.getPriceJobReport().getPriceJobSalaries().size() == 5);
    }

    @Test
    public void testSavePriceJobSurvey() throws Exception {
        PriceJobSurvey priceJobSurvey = new PriceJobSurvey();
        priceJobSurvey.setPriceJobId(1432523709005L);
        priceJobSurvey.setIsAccurate(true);
        priceJobSurvey.setIsUnderstandable(true);
        priceJobSurvey.setFeedback("Good Report");
        boolean isSaved = userEvaluationService.savePriceJobSurvey(priceJobSurvey);
        assertTrue(isSaved == true);
    }

    @Test
    public void testSavePriceJobSurveyNotSave() throws Exception {
        PriceJobSurvey priceJobSurvey = new PriceJobSurvey();
        priceJobSurvey.setPriceJobId(0L);
        priceJobSurvey.setIsAccurate(true);
        priceJobSurvey.setIsUnderstandable(true);
        priceJobSurvey.setFeedback("Bad Report");
        boolean isSaved = userEvaluationService.savePriceJobSurvey(priceJobSurvey);
        assertTrue(isSaved == false);
    }
}