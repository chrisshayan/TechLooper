package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.service.UserEvaluationService;
import com.techlooper.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchConfiguration.class, ElasticsearchUserImportConfiguration.class})
public class UserEvaluationServiceImplTest {

    @Resource
    private UserEvaluationService userEvaluationService;

    @Resource
    private UserService userService;

    @Test
    public void testScore() throws Exception {
        UserImportEntity userImportEntity = userService.findUserImportByEmail("baothaingo@gmail.com");
        long score = userEvaluationService.score(userImportEntity);
        assertTrue(score > 4000);
    }

    @Test
    public void testRate() throws Exception {
        UserImportEntity userImportEntity = userService.findUserImportByEmail("baothaingo@gmail.com");
        double rate = userEvaluationService.rate(userImportEntity);
        assertTrue(rate > 0 && rate <= 5);
    }

    @Test
    public void testRank() throws Exception {

    }
}