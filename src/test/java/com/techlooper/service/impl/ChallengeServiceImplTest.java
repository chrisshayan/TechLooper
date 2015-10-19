package com.techlooper.service.impl;

import com.techlooper.config.BaseConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.Language;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Random;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchConfiguration.class, BaseConfigurationTest.class})
public class ChallengeServiceImplTest {

    private static final int NUMBER_OF_ITEMS = 50;

    @Resource
    private ChallengeRegistrantRepository challengeRegistrantRepository;

    @Test
    public void dumpChallengeRegistrant() throws Exception {
        ChallengeRegistrantEntity challengeRegistrantEntity = new ChallengeRegistrantEntity();
        Random randomGenerator = new Random();

        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            int randomInt = randomGenerator.nextInt(1000000);
            challengeRegistrantEntity.setRegistrantId(Long.valueOf(randomInt));
            challengeRegistrantEntity.setChallengeId(1436281884654L);
            challengeRegistrantEntity.setRegistrantEmail("thu.hoang@navigosgroup.com");
            challengeRegistrantEntity.setRegistrantFirstName("Thu");
            challengeRegistrantEntity.setRegistrantLastName("Hoang");
            challengeRegistrantEntity.setLang(Language.en);
            challengeRegistrantEntity.setMailSent(true);
            ChallengeRegistrantEntity registrantEntity = challengeRegistrantRepository.save(challengeRegistrantEntity);
            assertTrue(registrantEntity != null);
            Thread.sleep(500);
        }
    }

}