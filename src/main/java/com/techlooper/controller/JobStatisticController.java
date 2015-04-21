package com.techlooper.controller;

import com.techlooper.model.*;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.JobStatisticService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class JobStatisticController {

  @Resource
  private JobStatisticService vietnamWorksJobStatisticService;

  @Resource
  private SimpMessagingTemplate messagingTemplate;

  @Resource
  private JsonConfigRepository jsonConfigRepository;

  @Resource
  private JobStatisticService jobStatisticService;

  @Scheduled(cron = "${scheduled.cron}")
  public void countTechnicalJobs() {
    jsonConfigRepository.getSkillConfig().stream().forEach(term -> {
        Map<String, Double> avgSalary = vietnamWorksJobStatisticService.getAverageSalaryBySkill(term);
        messagingTemplate.convertAndSend("/topic/jobs/term/" + term.getKey(), new TechnicalTermResponse.Builder()
          .withTerm(term.getKey()).withLabel(term.getLabel())
          .withAverageSalaryMin(avgSalary.get("SALARY_MIN"))
          .withAverageSalaryMax(avgSalary.get("SALARY_MAX"))
          .withCount(vietnamWorksJobStatisticService.count(term)).build());
      }
    );
  }

  @SendTo("/topic/jobs/terms")
  @MessageMapping("/jobs/terms")
  public List<TechnicalTermResponse> countTechnicalTerms() {
    List<TechnicalTermResponse> terms = new LinkedList<>();
    jsonConfigRepository.getSkillConfig().stream().forEach(term -> {
      Map<String, Double> avgSalary = vietnamWorksJobStatisticService.getAverageSalaryBySkill(term);
      terms.add(new TechnicalTermResponse.Builder().withTerm(term.getKey()).withLabel(term.getLabel())
        .withCount(vietnamWorksJobStatisticService.count(term))
        .withAverageSalaryMin(avgSalary.get("SALARY_MIN"))
        .withAverageSalaryMax(avgSalary.get("SALARY_MAX"))
        .build());
    });
    return terms;
  }

  @MessageMapping("/jobs")
  public void countTechnicalJobs(JobStatisticRequest request) {
    messagingTemplate.convertAndSend(
      "/topic/jobs/" + request.getTerm().toLowerCase(),
      new JobStatisticResponse.Builder().withCount(
        vietnamWorksJobStatisticService.count(
          jsonConfigRepository.findByKey(request.getTerm().toUpperCase())))
        .build());
  }

  @SendTo("/topic/analytics/skill")
  @MessageMapping("/analytics/skill")
  public SkillStatisticResponse countTechnicalSkillByTerm(SkillStatisticRequest skillStatisticRequest) {
    return vietnamWorksJobStatisticService.countJobsBySkill(
      jsonConfigRepository.findByKey(skillStatisticRequest.getTerm()),
      skillStatisticRequest.getHistograms());
  }

  @RequestMapping("/term/statistic")
  public TermStatisticResponse analyticTermBySkill(@RequestBody TermStatisticRequest termStatisticRequest) {
    return jobStatisticService.generateTermStatistic(termStatisticRequest, HistogramEnum.ONE_YEAR);
  }
}
