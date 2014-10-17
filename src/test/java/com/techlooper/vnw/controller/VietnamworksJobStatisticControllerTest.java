package com.techlooper.vnw.controller;

import com.techlooper.controller.JobsController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.techlooper.service.JobStatisticService;

public class VietnamworksJobStatisticControllerTest {

   @Mock
   private JobStatisticService vietnamWorksJobStatisticService;

   @InjectMocks
   private JobsController controller;

   @Mock
   private SimpMessagingTemplate messagingTemplate;

   @Before
   public void setup() {
      MockitoAnnotations.initMocks(this);
   }

   @Test
   public void testCountAllTechnicalJobs() throws Exception {
//      for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
//         Mockito.when(vietnamWorksJobStatisticService.count(term)).thenReturn(2L);
//      }
//      controller.countTechnicalJobs();
//      Mockito.verify(vietnamWorksJobStatisticService, Mockito.times(1)).countTechnicalJobs();
   }
}
