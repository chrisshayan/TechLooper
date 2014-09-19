package com.techlooper.controller;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.techlooper.model.JobStatisticResponse;
import com.techlooper.service.JobStatisticService;

@Controller
public class VietnamworksJobStatisticController {

   @Resource
   private JobStatisticService vietnamWorksJobStatisticService;

   @Resource
   private SimpMessagingTemplate messagingTemplate;

   @Scheduled(cron = "${scheduled.cron}")
   public void countJavaJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/java",
            new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countJavaJobs()).build());
   }

   @Scheduled(cron = "${scheduled.cron}")
   public void countRubyJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/ruby",
            new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countRubyJobs()).build());
   }

   @Scheduled(cron = "${scheduled.cron}")
   public void countQAJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/qa",
            new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countQAJobs()).build());
   }

   @Scheduled(cron = "${scheduled.cron}")
   public void countPythonJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/python",
            new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countPythonJobs()).build());
   }

   @Scheduled(cron = "${scheduled.cron}")
   public void countProjectManagerJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/pm", new JobStatisticResponse.Builder()
            .withCount(vietnamWorksJobStatisticService.countProjectManagerJobs()).build());
   }

   @Scheduled(cron = "${scheduled.cron}")
   public void countPhpJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/php",
            new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countPhpJobs()).build());
   }

   @Scheduled(cron = "${scheduled.cron}")
   public void countDBAJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/dba",
            new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countDBAJobs()).build());
   }

   @Scheduled(cron = "${scheduled.cron}")
   public void countBAJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/ba",
            new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countBAJobs()).build());
   }

   @Scheduled(cron = "${scheduled.cron}")
   public void countDotNetJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/dotnet",
            new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countDotNetJobs()).build());
   }

   @Scheduled(cron = "${scheduled.cron}")
   public void countTechnicalJobs() {
      messagingTemplate.convertAndSend("/topic/technical-job/all",
            new JobStatisticResponse.Builder().withCount(vietnamWorksJobStatisticService.countTechnicalJobs()).build());
   }
}
