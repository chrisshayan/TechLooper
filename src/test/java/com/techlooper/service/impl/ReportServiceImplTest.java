package com.techlooper.service.impl;

import com.techlooper.config.BaseConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeRegistrantPhaseItem;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeRegistrantService;
import com.techlooper.service.ReportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by phuonghqh on 11/30/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchConfiguration.class, BaseConfigurationTest.class})
public class ReportServiceImplTest  {

  private ReportService reportService = new ReportServiceImpl();

  @Resource
  private ElasticsearchTemplate elasticsearchTemplate;

  private ChallengeRegistrantService challengeRegistrantService = new ChallengeRegistrantServiceImpl();

  @Resource
  private ChallengeRepository challengeRepository;

  @Before
  public void before() {
    ReflectionTestUtils.setField(challengeRegistrantService, "elasticsearchTemplate", elasticsearchTemplate, ElasticsearchTemplate.class);

    ReflectionTestUtils.setField(reportService, "challengeRegistrantService", challengeRegistrantService, ChallengeRegistrantService.class);
    ReflectionTestUtils.setField(reportService, "challengeRepository", challengeRepository, ChallengeRepository.class);
  }

  @Test
  public void testGenerateFinalChallengeReport() {
    reportService.generateFinalChallengeReport("thu.hoang@navigosgroup.com", 1442316534178L);
  }
}
