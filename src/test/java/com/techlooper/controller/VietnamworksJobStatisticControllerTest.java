package com.techlooper.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobStatisticService;

@RunWith(SpringJUnit4ClassRunner.class)
@Configuration
@ContextConfiguration(classes = { VietnamworksJobStatisticControllerTest.class })
public class VietnamworksJobStatisticControllerTest {

   @Value("classpath:json/VietnamworksJobStatisticControllerTest.json")
   private Resource jsonResource;

   @Mock
   private JobStatisticService vietnamWorksJobStatisticService;

   @InjectMocks
   private VietnamworksJobStatisticController controller;

   private MockMvc mockMvc;

   @Before
   public void setup() {
      MockitoAnnotations.initMocks(this);
      mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
   }

   @Test
   public void testCountTechnicalJobs() throws Exception {
      for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
         Mockito.when(vietnamWorksJobStatisticService.count(term)).thenReturn(2L);
      }

      String expectedJson = Files.readAllLines(Paths.get(jsonResource.getURI())).stream()
            .collect(Collectors.joining(""));
      mockMvc.perform(get("/technical-job")).andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8")).andExpect(content().json(expectedJson));

      for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
         Mockito.verify(vietnamWorksJobStatisticService, Mockito.times(1)).count(term);
      }
   }
}
