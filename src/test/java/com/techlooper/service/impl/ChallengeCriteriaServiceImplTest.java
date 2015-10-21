package com.techlooper.service.impl;

import com.techlooper.config.BaseConfigurationTest;
import com.techlooper.config.DozerBeanConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.entity.ChallengeCriteria;
import com.techlooper.entity.ChallengeEntity;
import com.techlooper.entity.ChallengeRegistrantCriteria;
import com.techlooper.entity.ChallengeRegistrantEntity;
import com.techlooper.model.ChallengeCriteriaDto;
import com.techlooper.repository.elasticsearch.ChallengeRegistrantRepository;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeCriteriaService;
import com.techlooper.service.ChallengeService;
import org.dozer.Mapper;
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
@ContextConfiguration(classes = {BaseConfigurationTest.class, DozerBeanConfigurationTest.class, ElasticsearchConfiguration.class})
public class ChallengeCriteriaServiceImplTest {

  private ChallengeCriteriaService challengeCriteriaService;

  private ChallengeService challengeService;

  @Resource
  private ChallengeRepository challengeRepository;

  @Resource
  private ChallengeRegistrantRepository challengeRegistrantRepository;

  @Resource
  private Mapper dozerMapper;

  @Before
  public void before() {
    challengeService = new ChallengeServiceImpl();
    ReflectionTestUtils.setField(challengeService, "challengeRepository", challengeRepository);

    challengeCriteriaService = new ChallengeCriteriaServiceImpl();
    ReflectionTestUtils.setField(challengeCriteriaService, "challengeService", challengeService);
    ReflectionTestUtils.setField(challengeCriteriaService, "challengeRegistrantRepository", challengeRegistrantRepository);
    ReflectionTestUtils.setField(challengeCriteriaService, "challengeRepository", challengeRepository);
    ReflectionTestUtils.setField(challengeCriteriaService, "dozerMapper", dozerMapper);
  }

  @Test
  public void testEmpty() {
  }

  public void testSaveChallengeCriteria() {
    Long id = 1436339203843L;
    String owner = "thu.hoang@navigosgroup.com";

    Set<ChallengeCriteria> defaultCriterias = new HashSet<>();
    defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withName("UI/UX implemented solution").withWeight(25L).withCriteriaId("1").build());
    defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withName("Creativity on the proposed solution").withWeight(25L).withCriteriaId("2").build());
    defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withName("Source code quality").withWeight(25L).withCriteriaId("3").build());
    defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withName("Usage of top edge technology").withWeight(25L).withCriteriaId("4").build());
    defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withName("Application functionality").withWeight(25L).withCriteriaId("5").build());

    Long registrantId = 27L;
    ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(registrantId);
    long notNullScoreCount = registrant.getCriteria().stream().filter(cri -> cri.getScore() != null).count();

    ChallengeCriteriaDto challengeCriteriaDto = ChallengeCriteriaDto.ChallengeCriteriaDtoBuilder.challengeCriteriaDto()
      .withChallengeId(id).withChallengeCriteria(defaultCriterias).build();

    challengeCriteriaDto = challengeCriteriaService.saveChallengeCriteria(challengeCriteriaDto, owner);
    ChallengeEntity challenge = challengeRepository.findOne(id);
    Assert.assertEquals(challenge.getCriteria().size(), defaultCriterias.size());

    challengeCriteriaDto.getRegistrantCriteria().forEach(registrantCriteria -> {
      Assert.assertEquals(registrantCriteria.getCriteria().size(), defaultCriterias.size());
    });

    registrant = challengeRegistrantRepository.findOne(registrantId);
    if (registrant != null) {
      Assert.assertEquals(registrant.getCriteria().size(), registrant.getCriteria().size());
      long newNotNullScoreCount = registrant.getCriteria().stream().filter(cri -> cri.getScore() != null).count();
      Assert.assertEquals(newNotNullScoreCount, notNullScoreCount);
    }
  }

  public void testEditChallengeCriteria() {
    Long id = 1436339203843L;
    String owner = "thu.hoang@navigosgroup.com";
    ChallengeEntity challenge = challengeRepository.findOne(id);
    Set<ChallengeCriteria> challengeCriteria = challenge.getCriteria();

    ChallengeCriteria sampleCriteria = ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withCriteriaId("sample").withName("sample name").withWeight(100L).build();
    challengeCriteria.add(sampleCriteria);
    challenge = challengeRepository.save(challenge);

    ChallengeCriteriaDto challengeCriteriaDto = ChallengeCriteriaDto.ChallengeCriteriaDtoBuilder.challengeCriteriaDto()
      .withChallengeId(id).withChallengeCriteria(challengeCriteria).build();
    challengeCriteriaService.saveChallengeCriteria(challengeCriteriaDto, owner);

    Long registrantId = 27L;
    ChallengeRegistrantEntity registrant = challengeRegistrantRepository.findOne(registrantId);
    if (registrant != null) {
      registrant.getCriteria().stream().filter(cri -> cri.getCriteriaId().equals(sampleCriteria.getCriteriaId())).findFirst().get().setScore(100L);
      registrant = challengeRegistrantRepository.save(registrant);

      sampleCriteria.setName("sample name changed");
      challengeCriteriaService.saveChallengeCriteria(challengeCriteriaDto, owner);

      registrant = challengeRegistrantRepository.findOne(registrantId);
      ChallengeRegistrantCriteria registrantCriteria = registrant.getCriteria().stream()
        .filter(cri -> cri.getName().equals(sampleCriteria.getName())).findFirst().get();
      Assert.assertNotNull(registrantCriteria);
    }
  }
}
