package com.techlooper.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobStatisticService;

public class VietnamworksJobStatisticControllerTest {

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

      mockMvc.perform(get("/technical-job")).andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"));

      for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
         Mockito.verify(vietnamWorksJobStatisticService, Mockito.times(1)).count(term);
      }
   }
}
