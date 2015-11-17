package com.techlooper.integration;

import com.techlooper.config.BaseConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeService;
import com.techlooper.service.impl.ChallengeServiceImpl;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 8/11/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BaseConfigurationTest.class, ElasticsearchConfiguration.class})
public class ChallengeServiceIntegrationTest {

    private ChallengeService challengeService;

    @Resource
    private ChallengeRepository challengeRepository;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Value("${elasticsearch.userimport.index.name}")
    private String techlooperIndex;


    @Before
    public void before() {
        challengeService = new ChallengeServiceImpl();
        ReflectionTestUtils.setField(challengeService, "challengeRepository", challengeRepository);
        ReflectionTestUtils.setField(challengeService, "challengeRegistrantRepository", challengeRegistrantRepository);
        ReflectionTestUtils.setField(challengeService, "techlooperIndex", techlooperIndex);
        ReflectionTestUtils.setField(challengeService, "dozerMapper", new DozerBeanMapper());
    }

}
