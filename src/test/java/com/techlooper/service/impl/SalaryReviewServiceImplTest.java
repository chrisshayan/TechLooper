package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.entity.SalaryReview;
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
        List<SalaryReview> salaryReviews =
                salaryReviewService.searchSalaryReview("Java Developer", Arrays.asList(35L, 55L, 57L));
        assertTrue(salaryReviews.size() > 0);
    }
}