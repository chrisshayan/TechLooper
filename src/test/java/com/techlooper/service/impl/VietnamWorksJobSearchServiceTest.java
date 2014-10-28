package com.techlooper.service.impl;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.model.VNWConfigurationResponse;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VNWJobSearchResponse;
import com.techlooper.service.JobSearchService;
import com.techlooper.util.JsonUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class})
public class VietnamWorksJobSearchServiceTest {

    @Resource
    private JobSearchService jobSearchService;

    @Resource
    private String vnwJobSearchRequestJson;

    private VNWJobSearchRequest vnwJobSearchRequest;

    @Before
    public void setUp() {
        assertNotNull(jobSearchService);
        vnwJobSearchRequest = JsonUtils.toPOJO(vnwJobSearchRequestJson, VNWJobSearchRequest.class);
    }

    @Test
    public void testGetConfiguration() {
        VNWConfigurationResponse vnwConfigurationResponse = jobSearchService.getConfiguration();

        assertNotNull(vnwConfigurationResponse);

        assertNotEquals("Number Of Locations : " + vnwConfigurationResponse.getData().getLocations().size(),
          0, vnwConfigurationResponse.getData().getLocations().size());

        assertNotEquals("Number Of Degrees : " + vnwConfigurationResponse.getData().getDegrees().size(),
          0, vnwConfigurationResponse.getData().getDegrees().size());
    }

    @Test
    public void testSearchJob() {
        VNWJobSearchResponse vnwConfigurationResponse = jobSearchService.searchJob(vnwJobSearchRequest);

        assertNotNull(vnwConfigurationResponse);

        int totalJob = vnwConfigurationResponse.getData().getTotal();
        assertNotEquals("Total Jobs : " + totalJob, 0, totalJob);
    }

    @Test
    public void testSearchJobEmptyData() {
        vnwJobSearchRequest.setJobTitle("AngularJS,Java");
        VNWJobSearchResponse vnwConfigurationResponse = jobSearchService.searchJob(vnwJobSearchRequest);

        assertNotNull(vnwConfigurationResponse);

        Integer totalJob = vnwConfigurationResponse.getData().getTotal();
        assertEquals("Total Jobs : " + totalJob.longValue(), 0, totalJob.longValue());
    }

    @After
    public void tearDown() {
        vnwJobSearchRequest = null;
    }
}
