package com.techlooper.controller;

import com.techlooper.enu.RouterConstant;
import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.model.*;
import com.techlooper.service.JobStatisticService;
import org.apache.camel.Consume;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RecipientList;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Controller
public class JobsController {

  @Resource
  private JobStatisticService vietnamWorksJobStatisticService;

  @Resource
  private SimpMessagingTemplate messagingTemplate;


  @Produce(uri = "direct:jobs/search")
  private ProducerTemplate jobsSearchProducer;


  @MessageMapping("/jobs/search")
  public void searchJobs(JobSearchRequest searchJobsRequest) {
    jobsSearchProducer.sendBodyAndHeader(searchJobsRequest, RouterConstant.TO, RouterConstant.VIETNAMWORKS);
  }

  @SendTo("/topic/jobs/search")
  @Consume(uri = "direct:jobs/search/response")
  @RecipientList
  public void replySearchJobs(JobSearchResponse jobSearchResponse) {
    messagingTemplate.convertAndSend("/topic/jobs/search", jobSearchResponse);
  }


  @Scheduled(cron = "${scheduled.cron}")
  public void countTechnicalJobs() {
    for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
      messagingTemplate.convertAndSend("/topic/jobs/term/" + term.name(), new JobStatisticResponse.Builder()
        .withCount(vietnamWorksJobStatisticService.count(term)).build());
    }
  }

  /**
   * need to think a way how fetch TechnicalTerms dynamic, we might rework on
   * business model
   */
  @SendTo("/topic/jobs/terms")
  @MessageMapping("/jobs/terms")
  public List<TechnicalTermResponse> countTechnicalTerms() {
    List<TechnicalTermResponse> terms = new LinkedList<TechnicalTermResponse>();
    for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
      terms.add(new TechnicalTermResponse.Builder().withTerm(term)
        .withCount(vietnamWorksJobStatisticService.count(term)).build());
    }
    return terms;
  }

  @MessageMapping("/jobs")
  public void countTechnicalJobs(JobStatisticRequest request) {
    messagingTemplate.convertAndSend(
      "/topic/jobs/" + request.getTerm().toLowerCase(),
      new JobStatisticResponse.Builder().withCount(
        vietnamWorksJobStatisticService.count(TechnicalTermEnum.valueOf(request.getTerm().toUpperCase())))
        .build());
  }
}
