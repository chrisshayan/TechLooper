package com.techlooper.controller;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
   private SimpMessageSendingOperations messagingTemplate;

   @SendTo("/topic/technical-job")
   @MessageMapping("/technical-job")
   public JobStatisticResponse countJobs(JobStatisticResquest requestTerm) {
      return new JobStatisticResponse.Builder().withCount(
            vietnamWorksJobStatisticService.count(TechnicalTermEnum.valueOf(requestTerm.getTerm()))).build();
   }
}
