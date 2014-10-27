package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobStatisticService;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

import static org.junit.Assert.assertThat;

/**
 * Created by chrisshayan on 7/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchConfiguration.class})
public class VietnamWorksJobStatisticServiceTest {

  private JobStatisticService jobStatisticService;

  @Resource
  private ElasticsearchTemplate elasticsearchTemplate;

  @Before
  public void before() {
    jobStatisticService = new VietnamWorksJobStatisticService();
    ReflectionTestUtils.setField(jobStatisticService, "elasticsearchTemplate", elasticsearchTemplate);
  }

  @Test
  public void countQAJobs() {
    assertThat(jobStatisticService.countQAJobs(), IsNot.not(IsNull.nullValue()));
  }

  @Test
  public void countBAJobs() {
    assertThat(jobStatisticService.countBAJobs(), IsNot.not(IsNull.nullValue()));
  }

  @Test
  public void countDBAJobs() {
    assertThat(jobStatisticService.countDBAJobs(), IsNot.not(IsNull.nullValue()));
  }

  @Test
  public void countPythonJobs() {
    assertThat(jobStatisticService.countPythonJobs(), IsNot.not(IsNull.nullValue()));
  }

  @Test
  public void countRubyJobs() {
    assertThat(jobStatisticService.countRubyJobs(), IsNot.not(IsNull.nullValue()));
  }

  @Test
  public void countProjectManagerJobs() {
    assertThat(jobStatisticService.countProjectManagerJobs(), IsNot.not(IsNull.nullValue()));
  }

  @Test
  public void countPhpJobs() {
    assertThat(jobStatisticService.countPhpJobs(), IsNot.not(IsNull.nullValue()));
  }

  @Test
  public void countJavaJobs() {
    assertThat(jobStatisticService.countJavaJobs(), IsNot.not(IsNull.nullValue()));
  }

  @Test
  public void countDotNetJobs() {
    assertThat(jobStatisticService.countDotNetJobs(), IsNot.not(IsNull.nullValue()));
  }

  @Test
  public void countJava() {
    assertThat(jobStatisticService.count(TechnicalTermEnum.JAVA), Is.is(jobStatisticService.countJavaJobs()));
  }

  @Test
  public void countPhp() {
    assertThat(jobStatisticService.count(TechnicalTermEnum.PHP), Is.is(jobStatisticService.countPhpJobs()));
  }

  @Test
  public void countNet() {
    assertThat(jobStatisticService.count(TechnicalTermEnum.DOTNET), Is.is(jobStatisticService.countDotNetJobs()));
  }
}
