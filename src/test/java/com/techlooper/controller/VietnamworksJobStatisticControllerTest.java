package com.techlooper.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobStatisticService;

public class VietnamworksJobStatisticControllerTest {

   @Mock
   private JobStatisticService vietnamWorksJobStatisticService;

   @InjectMocks
   private VietnamworksJobStatisticController controller;

   @Mock
   private SimpMessagingTemplate messagingTemplate;

   @Before
   public void setup() {
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void testCountTechnicalJobs() throws Exception {
      // JobStatisticResquest requestTerm = new
      // JobStatisticResquest.Builder().withTerm("JAVA").build();
      // Mockito.when(vietnamWorksJobStatisticService.count(TechnicalTermEnum.JAVA)).thenReturn(2L);
      // controller.countTechnicalJobs(requestTerm);
      // Mockito.verify(vietnamWorksJobStatisticService,
      // Mockito.times(1)).count(TechnicalTermEnum.JAVA);
   }

   @Test
   public void testCountAllTechnicalJobs() throws Exception {
      for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
         Mockito.when(vietnamWorksJobStatisticService.count(term)).thenReturn(2L);
      }
      controller.countTechnicalJobs();
      Mockito.verify(vietnamWorksJobStatisticService, Mockito.times(1)).countTechnicalJobs();
   }
}
