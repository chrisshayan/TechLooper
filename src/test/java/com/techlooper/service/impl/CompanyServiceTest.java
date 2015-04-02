package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.entity.CompanyEntity;
import com.techlooper.service.CompanyService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 4/2/15.
 */
@Configuration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchUserImportConfiguration.class, CompanyServiceTest.class})
public class CompanyServiceTest {

  @Resource
  private CompanyService target;

  @Bean
  public CompanyService companyService() {
    return new CompanyServiceImpl();
  }

  @Test
  @Ignore
  public void testFindById() {
    CompanyEntity company = target.findById(22538L);
    Assert.assertEquals(22538, company.getCompanyId().longValue());
  }

  @Test
  public void testFindByName() {
    CompanyEntity company = target.findByName("Fujinet Co.");
    Assert.assertEquals("Fujinet Co.", company.getCompanyName());
  }
}
