package com.techlooper.controller;

import com.techlooper.model.*;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.JobStatisticService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.*;

@Controller
public class JobsController {

  @Resource
  private JobStatisticService vietnamWorksJobStatisticService;

  @Resource
  private JobSearchService vietnamWorksJobSearchService;

  @Resource
  private SimpMessagingTemplate messagingTemplate;

  @Value("${vnw.api.configuration.category.it.software.en}")
  private String category;


  @SendTo("/topic/jobs/search")
  @MessageMapping("/jobs/search")
  public JobSearchResponse searchJobs(JobSearchRequest searchJobsRequest) {
    final VNWJobSearchRequest vnwJobSearchRequest = convertToVNWJobSearchRequest(searchJobsRequest);
    final VNWJobSearchResponse vnwJobSearchResponse = vietnamWorksJobSearchService.searchJob(vnwJobSearchRequest);
    return convertToJobSearchResponse(vnwJobSearchResponse);
  }


  @Scheduled(cron = "${scheduled.cron}")
  public void countTechnicalJobs() {
    Arrays.stream(TechnicalTermEnum.values()).forEach(term -> {
      messagingTemplate.convertAndSend("/topic/jobs/term/" + term.name(), new JobStatisticResponse.Builder()
        .withCount(vietnamWorksJobStatisticService.count(term)).build());
    });
  }

  /**
   * need to think a way how fetch TechnicalTerms dynamic, we might rework on
   * business model
   */
  @SendTo("/topic/jobs/terms")
  @MessageMapping("/jobs/terms")
  public List<TechnicalTermResponse> countTechnicalTerms() {
    List<TechnicalTermResponse> terms = new LinkedList<TechnicalTermResponse>();
    Arrays.stream(TechnicalTermEnum.values()).forEach(term -> {
      terms.add(new TechnicalTermResponse.Builder().withTerm(term)
        .withCount(vietnamWorksJobStatisticService.count(term)).build());
    });
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

  private VNWJobSearchRequest convertToVNWJobSearchRequest(JobSearchRequest jobSearchRequest) {
    return new VNWJobSearchRequest(
      jobSearchRequest.getTerms(),
      category,
      jobSearchRequest.getPageNumber()
    );
  }

  private JobSearchResponse convertToJobSearchResponse(VNWJobSearchResponse vnwJobSearchResponse) {
    final JobSearchResponse jobSearchResponse = new JobSearchResponse();
    jobSearchResponse.setTotal(vnwJobSearchResponse.getData().getTotal());
    final Set<JobResponse> jobs = new HashSet<>();
    vnwJobSearchResponse.getData().getJobs().stream().forEach(item -> {
      final JobResponse.Builder builder = new JobResponse.Builder();
      jobs.add(builder.withCompany(item.getCompany())
        .withLevel(item.getLevel())
        .withLocation(item.getLocation())
        .withLogoUrl(item.getLogoUrl())
        .withPostedOn(item.getPostedOn())
        .withTitle(item.getTitle())
        .withUrl(item.getUrl())
        .withVideoUrl(item.getVideoUrl())
        .build());
    });
    jobSearchResponse.setJobs(jobs);
    return jobSearchResponse;
  }
}
