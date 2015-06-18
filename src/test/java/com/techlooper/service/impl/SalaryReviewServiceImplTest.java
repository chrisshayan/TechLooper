package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.entity.SalaryReviewEntity;
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
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchConfiguration.class, ElasticsearchUserImportConfiguration.class})
public class SalaryReviewServiceImplTest {

    @Resource
    private SalaryReviewService salaryReviewService;

    @Test
    public void testSearchSalaryReview() throws Exception {
        SalaryReviewEntity salaryReviewEntity = new SalaryReviewEntity();
        salaryReviewEntity.setJobTitle("Java Developer");
        salaryReviewEntity.setJobCategories(Arrays.asList(35L, 55L, 57L));
        List<SalaryReviewEntity> salaryReviewEntities = salaryReviewService.searchSalaryReview(salaryReviewEntity);
        assertTrue(salaryReviewEntities.size() > 0);
    }

    @Test
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