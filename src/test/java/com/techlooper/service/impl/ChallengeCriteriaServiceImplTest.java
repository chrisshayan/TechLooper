package com.techlooper.service.impl;

import com.techlooper.config.BaseConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.entity.ChallengeCriteria;
import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengeCriteriaDto;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeCriteriaService;
import com.techlooper.service.ChallengeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by phuonghqh on 10/19/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BaseConfigurationTest.class, ElasticsearchConfiguration.class})
public class ChallengeCriteriaServiceImplTest {

  private ChallengeCriteriaService challengeCriteriaService;

  private ChallengeService challengeService;

  @Resource
  private ChallengeRepository challengeRepository;

  @Before
  public void before() {
    challengeService = new ChallengeServiceImpl();
    ReflectionTestUtils.setField(challengeService, "challengeRepository", challengeRepository);

    challengeCriteriaService = new ChallengeCriteriaServiceImpl();
    ReflectionTestUtils.setField(challengeCriteriaService, "challengeService", challengeService);
    ReflectionTestUtils.setField(challengeCriteriaService, "challengeRepository", challengeRepository);
  }

  @Test
  public void testSaveChallengeCriterias() {
    Long id = 1444017901257L;
    String owner = "thu.hoang@navigosgroup.com";

    Set<ChallengeCriteria> challengeCriterias = new HashSet<>();
    for (int i = 0; i < 5; i++) {
      challengeCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
        .withName("name 1").withWeight(Long.valueOf(i + 10)).build());
    }

    ChallengeCriteriaDto challengeCriteriaDto = ChallengeCriteriaDto.ChallengeCriteriaDtoBuilder.challengeCriteriaDto()
      .withChallengeId(id).withChallengeCriterias(challengeCriterias).build();

    ChallengeEntity challenge = challengeCriteriaService.saveChallengeCriterias(challengeCriteriaDto, owner);
    Assert.assertTrue(challenge.getChallengeCriterias().size() >= 5);
  }
}
