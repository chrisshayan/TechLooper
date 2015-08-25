package com.techlooper.integration;

import com.techlooper.config.BaseConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.dto.WebinarInfoDto;
import com.techlooper.repository.elasticsearch.WebinarRepository;
import com.techlooper.service.WebinarService;
import com.techlooper.service.impl.WebinarServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Created by phuonghqh on 8/17/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BaseConfigurationTest.class, ElasticsearchConfiguration.class})
public class WebinarServiceTest {

  private WebinarService webinarService;

  @Resource
  private WebinarRepository webinarRepository;

  @Resource
  private ElasticsearchTemplate elasticsearchTemplate;

  @Before
  public void before() {
    webinarService = new WebinarServiceImpl();
    ReflectionTestUtils.setField(webinarService, "webinarRepository", webinarRepository);
    ReflectionTestUtils.setField(webinarService, "elasticsearchTemplate", elasticsearchTemplate);
  }

  @Test
  public void testFindAvailableWebinars() {
    Collection<WebinarInfoDto> list = webinarService.findAvailableWebinars();
    System.out.println(list);
  }
}
