package com.techlooper.controller;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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

   @Scheduled(cron = "${scheduled.cron}")
   public void countTechnicalJobs() {
      for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
         messagingTemplate.convertAndSend("/topic/technical-job/" + term.name(), new JobStatisticResponse.Builder()
               .withCount(vietnamWorksJobStatisticService.count(term)).build());
      }
   }

   @SendTo("/topic/technical-job/terms")
   @MessageMapping("/technical-job/terms")
   public TechnicalTermEnum[] countTechnicalTerms() {
      return TechnicalTermEnum.values();
   }

   @MessageMapping("/technical-job")
   public void countTechnicalJobs(JobStatisticResquest request) {
      messagingTemplate.convertAndSend(
            "/topic/technical-job/" + request.getTerm().toLowerCase(),
            new JobStatisticResponse.Builder().withCount(
                  vietnamWorksJobStatisticService.count(TechnicalTermEnum.valueOf(request.getTerm().toUpperCase())))
                  .build());
   }

   @Scheduled(cron = "${scheduled.cron}")
   @MessageMapping("/technical-job/total")
   public void totalTechnicalJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/total",
            new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countTechnicalJobs()).build());
   }
}
