package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.model.PeriodEnum;
import com.techlooper.model.TechnicalSkillEnumMap;
import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobQueryBuilder;
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
import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by chrisshayan on 7/14/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchConfiguration.class})
public class VietnamWorksJobStatisticServiceTest {

  private JobStatisticService jobStatisticService;

  @Resource
  private ElasticsearchTemplate elasticsearchTemplate;

  @Resource
  private TechnicalSkillEnumMap technicalSkillEnumMap;

  @Resource
  private JobQueryBuilder jobQueryBuilder;

  @Before
  public void before() {
    jobStatisticService = new VietnamWorksJobStatisticService();
    ReflectionTestUtils.setField(jobStatisticService, "elasticsearchTemplate", elasticsearchTemplate);
    ReflectionTestUtils.setField(jobStatisticService, "technicalSkillEnumMap", technicalSkillEnumMap);
    ReflectionTestUtils.setField(jobStatisticService, "jobQueryBuilder", jobQueryBuilder);
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

  @Test
  public void shouldReturnZeroIfNotExistTechnicalTerm() {
    //Given
    TechnicalTermEnum term = null;
    //When
    Long count = jobStatisticService.countTechnicalJobsBySkill(term, "skill", LocalDate.now());
    //Then
    assertEquals(0, count.longValue());
  }

  @Test
  public void shouldReturnNumberOfSkillJobIfExistTechnicalTermAndCorrectSkill() {
    //Given
    TechnicalTermEnum term = TechnicalTermEnum.JAVA;
    //When
    Long count = jobStatisticService.countTechnicalJobsBySkill(term, "Spring", LocalDate.now());
    //Then
    assertNotEquals(0, count.longValue());
  }

  @Test
  public void shouldReturnNumberOfTechnicalJobIfExistTechnicalTermAndEmptySkill() {
    //Given
    TechnicalTermEnum term = TechnicalTermEnum.JAVA;
    //When
    Long count = jobStatisticService.countTechnicalJobsBySkill(term, "", LocalDate.now());
    //If input skill is empty, Then return the total of jobs matching its term (ex : JAVA)
    assertTrue(jobStatisticService.countJavaJobs().longValue() >= count.longValue());
  }

//  @Test
//  public void testCountJobsBySkills() {
//    jobStatisticService.countJobsBySkill(TechnicalTermEnum.JAVA, PeriodEnum.WEEK);
//  }
}
