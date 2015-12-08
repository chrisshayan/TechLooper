package com.techlooper.service.impl;

import com.techlooper.config.*;
import com.techlooper.model.SalaryReviewDto;
import com.techlooper.model.SalaryReviewResultDto;
import com.techlooper.model.SimilarSalaryReview;
import com.techlooper.model.SimilarSalaryReviewRequest;
import com.techlooper.service.SalaryReviewService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SalaryReviewServiceConfigurationTest.class, ElasticsearchConfiguration.class})
public class SalaryReviewServiceImplTest {

    @Resource
    private SalaryReviewService salaryReviewService;

    @Test
    public void testSearchSalaryReview() throws Exception {
        SalaryReviewDto salaryReviewDto = new SalaryReviewDto();
        salaryReviewDto.setJobTitle("Java Developer");
        salaryReviewDto.setJobCategories(Arrays.asList(35L));
        salaryReviewDto.setNetSalary(1000);
        SalaryReviewResultDto salaryReviewResult = salaryReviewService.reviewSalary(salaryReviewDto);
        assertTrue(salaryReviewResult.getSalaryReport().getPercentRank() > 0);
    }

    public void testGetSimilarSalaryReview() throws Exception {
        SimilarSalaryReviewRequest request = new SimilarSalaryReviewRequest();
        request.setJobTitle("Java Developer");
        request.setSkills(Arrays.asList("Spring", "Hibernate"));
        request.setJobCategories(Arrays.asList(35L, 55L));
        request.setJobLevelIds(Arrays.asList(5, 6));
        request.setCompanySizeId(3L);
        request.setLocationId(29L);
        request.setNetSalary(1000);
        List<SimilarSalaryReview> similarSalaryReviews = salaryReviewService.getSimilarSalaryReview(request);
        assertTrue(similarSalaryReviews.size() > 0 && similarSalaryReviews.size() <= 3);
        for (SimilarSalaryReview similarSalaryReview : similarSalaryReviews) {
            assertTrue(similarSalaryReview.getAddedPercent() > 0D);
        }
    }
}