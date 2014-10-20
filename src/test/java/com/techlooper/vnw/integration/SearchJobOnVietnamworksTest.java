package com.techlooper.vnw.integration;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.IntegrationConfiguration;
import com.techlooper.enu.RouterConstant;
import com.techlooper.model.JobSearchRequest;
import com.techlooper.model.JobSearchResponse;
import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 10/19/14.
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, IntegrationConfiguration.class}, loader = CamelSpringDelegatingTestContextLoader.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ComponentScan({"com.techlooper.integration", "com.techlooper.vnw.integration"})
public class SearchJobOnVietnamworksTest {

  @EndpointInject(uri = "mock:api-staging.vietnamworks.com/general/configuration")
  protected MockEndpoint mockConfiguration;

  @EndpointInject(uri = "mock:api-staging.vietnamworks.com/jobs/search")
  protected MockEndpoint mockJobSearch;

  @Produce(uri = "direct:jobs/search")
  private ProducerTemplate jobsSearchProducer;

  @Resource
  private String vnwConfigurationJson;

  @Resource
  private String vnwJobSearchJson;

  @Test
  @DirtiesContext
  public void testSearchJobOnVietnamworks() throws InterruptedException {
    JobSearchRequest searchJobsRequest = new JobSearchRequest();
    searchJobsRequest.setPageNumber("1");
    searchJobsRequest.setTerms("java");
    mockConfiguration.expectedBodiesReceived(vnwConfigurationJson);
    mockJobSearch.expectedBodiesReceived(vnwJobSearchJson);

    jobsSearchProducer.sendBodyAndHeader(searchJobsRequest, RouterConstant.TO, RouterConstant.VIETNAMWORKS);

//    mockConfiguration.assertIsSatisfied();
//    mockJobSearch.assertIsSatisfied();
  }
}
