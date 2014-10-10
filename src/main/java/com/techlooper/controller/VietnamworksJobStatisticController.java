package com.techlooper.controller;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.techlooper.model.JobStatisticRequest;
import com.techlooper.model.JobStatisticResponse;
import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.model.TechnicalTermResponse;
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
               .withCount(vietnamWorksJobStatisticService.count(term) + new java.util.Random().nextInt(100)).build());
      }
   }

   /**
    * need to think a way how fetch TechnicalTerms dynamic, we might rework on
    * business model
    */
   @SendTo("/topic/technical-job/terms")
   @MessageMapping("/technical-job/terms")
   public List<TechnicalTermResponse> countTechnicalTerms() {
      List<TechnicalTermResponse> terms = new LinkedList<TechnicalTermResponse>();
      for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
         terms.add(new TechnicalTermResponse.Builder().withTerm(term)
               .withCount(vietnamWorksJobStatisticService.count(term)).build());
      }
      return terms;
   }

   @MessageMapping("/technical-job")
   public void countTechnicalJobs(JobStatisticRequest request) {
      messagingTemplate.convertAndSend(
            "/topic/technical-job/" + request.getTerm().toLowerCase(),
            new JobStatisticResponse.Builder().withCount(
                  vietnamWorksJobStatisticService.count(TechnicalTermEnum.valueOf(request.getTerm().toUpperCase())))
                  .build());
   }
}
