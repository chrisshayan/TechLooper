package com.techlooper.service.impl;

import com.techlooper.model.VNWConfigurationResponse;
import com.techlooper.repository.JobSearchAPIConfigurationRepository;
import com.techlooper.service.JobSearchService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

public class VietnamWorksJobSearchServiceTest {

    private String configurationUrl = "configurationUrl";
    private String apiKeyName = "apiKeyName";
    private String apiKeyValue = "apiKeyValue";

    private JobSearchService jobSearchService = new VietnamworksJobSearchService();
    private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    private JobSearchAPIConfigurationRepository apiConfigurationRepository = mock(JobSearchAPIConfigurationRepository.class);
    private HttpEntity<String> requestEntity;

    @Before
    public void before() throws Exception {
        ReflectionTestUtils.setField(jobSearchService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(jobSearchService, "apiConfiguration", apiConfigurationRepository);

        requestEntity = configureHttpRequestEntity(StringUtils.EMPTY);
        String vnwConfiguration = IOUtils.toString(getClass().getResourceAsStream("/expect/vnw-configuration.json"), "UTF-8");
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(vnwConfiguration, HttpStatus.OK);

        when(restTemplate.exchange(configurationUrl, HttpMethod.GET, requestEntity, String.class)).thenReturn(responseEntity);
        when(apiConfigurationRepository.getApiKeyName()).thenReturn(apiKeyName);
        when(apiConfigurationRepository.getApiKeyValue()).thenReturn(apiKeyValue);
        when(apiConfigurationRepository.getConfigurationUrl()).thenReturn(configurationUrl);
    }

    @Test
    public void testGetConfiguration() throws Exception {
        VNWConfigurationResponse vnwConfigurationResponse = jobSearchService.getConfiguration();

        assertNotNull(vnwConfigurationResponse);
    }

    @Test
    public void testGetResponseFromCache() throws Exception {// When call service twice
        jobSearchService.getConfiguration();
        jobSearchService.getConfiguration();

        // Then service should get response from cache instead of invoking rest template twice.
        // So the rest template should be invoked once at most
        verify(restTemplate, atMost(1)).exchange(configurationUrl, HttpMethod.GET, requestEntity, String.class);
    }

    private HttpEntity<String> configureHttpRequestEntity(String requestBody) {
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(APPLICATION_JSON));
        requestHeaders.set(apiKeyName, apiKeyValue);
        return new HttpEntity<>(requestBody, requestHeaders);
    }
}