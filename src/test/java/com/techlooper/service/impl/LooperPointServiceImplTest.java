package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.service.LooperPointService;
import com.techlooper.service.UserEvaluationService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 3/20/15.
 */
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchUserImportConfiguration.class, LooperPointServiceImplTest.class})
public class LooperPointServiceImplTest {

  @Resource(name = "elasticsearchTemplateUserImport")
  private ElasticsearchTemplate elasticsearchTemplateUserImport;

  @Mock
  private UserEvaluationService userEvaluationService;

  private LooperPointService looperPointService;

  @Value("${elasticsearch.userimport.index.name}")
  private String indexName;

  @Before
  public void before() {
    MockitoAnnotations.initMocks(this);
    looperPointService = new LooperPointServiceImpl();
    ReflectionTestUtils.setField(looperPointService, "userEvaluationService", userEvaluationService);
    ReflectionTestUtils.setField(looperPointService, "indexName", indexName);
    ReflectionTestUtils.setField(looperPointService, "elasticsearchTemplateUserImport", elasticsearchTemplateUserImport);
  }

  public void testEvaluateCandidates() {
    looperPointService.evaluateCandidates();
  }
}
