package com.techlooper.controller;

import java.util.HashMap;
import java.util.Map;

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

   private Map<String, Long> termMap = new HashMap<String, Long>();

   @Scheduled(cron = "${scheduled.cron}")
   public void countTechnicalJobs() {
      for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
         Long count = vietnamWorksJobStatisticService.count(term);
//         if (termMap.containsKey(term.name()) && termMap.get(term.name()) == count) {
//            continue;
//         }
//         termMap.put(term.name(), count);
         messagingTemplate.convertAndSend("/topic/technical-job/" + term.name(), new JobStatisticResponse.Builder()
               .withCount(count).build());
      }
   }

   /** need to think a way how fetch TechnicalTerms dynamic, we might rework on business model */
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
