package com.techlooper.service.impl;

import com.techlooper.model.vnw.VNWJobSearchRequest;
import com.techlooper.model.vnw.VNWJobSearchResponse;
import com.techlooper.service.JobSearchService;
import com.techlooper.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.core.Is;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/springContext-VietnamWorksJobSearchService-Test.xml")
public class VietnamWorksJobSearchServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JobSearchService jobSearchService;

    @Ignore
    public void testGetConfiguration() {
        jobSearchService.getConfiguration();
    }

    @Test
    public void testSearchJob() throws IOException {
        Resource requestResource = applicationContext.getResource("classpath:expect/vnw-jobs-request.json");
        Resource responseResource = applicationContext.getResource("classpath:expect/vnw-jobs.json");
        String requestJson = IOUtils.toString(requestResource.getInputStream());
        String responseJson = IOUtils.toString(responseResource.getInputStream());
        VNWJobSearchRequest vnwJobSearchRequest = JsonUtils.toPOJO(requestJson, VNWJobSearchRequest.class);
        VNWJobSearchResponse vnwJobSearchResponse = JsonUtils.toPOJO(responseJson, VNWJobSearchResponse.class);

        JobSearchService jobSearchServiceMock = Mockito.mock(jobSearchService.getClass());
        when(jobSearchServiceMock.searchJob(vnwJobSearchRequest)).thenReturn(vnwJobSearchResponse);
        
        assertThat(jobSearchServiceMock.searchJob(vnwJobSearchRequest).getData().getTotal(), Is.is(new Integer(100)));
    }
}
