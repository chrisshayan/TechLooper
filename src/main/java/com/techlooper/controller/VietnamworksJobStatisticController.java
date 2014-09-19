package com.techlooper.controller;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.techlooper.model.JobStatisticResponse;
import com.techlooper.model.JobStatisticResquest;
import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobStatisticService;

@Controller
public class VietnamworksJobStatisticController {

   @Resource
   private JobStatisticService vietnamWorksJobStatisticService;

   @Resource
   private SimpMessagingTemplate messagingTemplate;

   @SendTo("/topic/technical-job")
   @MessageMapping("/technical-job")
   public JobStatisticResponse countTechnicalJobs(JobStatisticResquest requestTerm) {
      return new JobStatisticResponse.Builder().withCount(
            vietnamWorksJobStatisticService.count(TechnicalTermEnum.valueOf(requestTerm.getTerm()))).build();
   }

   // @Scheduled(c)
   @SendTo("/topic/technical-job/count-all")
   @MessageMapping("/technical-job/count-all")
   public JobStatisticResponse count() {
      return new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countTechnicalJobs()).build();
      // messagingTemplate.convertAndSend("/topic/technical-job/all", new
      // JobStatisticResponse.Builder().withCount(
      // vietnamWorksJobStatisticService.countTechnicalJobs()).build());
   }
}
