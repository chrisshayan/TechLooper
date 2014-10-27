package com.techlooper.controller;

import com.techlooper.model.*;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VNWJobSearchResponse;
import com.techlooper.model.VNWJobSearchResponseDataItem;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.JobStatisticService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    //jobsSearchProducer.sendBodyAndHeader(searchJobsRequest, RouterConstant.TO, RouterConstant.VIETNAMWORKS);
    VNWJobSearchRequest vnwJobSearchRequest = convertToVNWJobSearchRequest(searchJobsRequest);
    VNWJobSearchResponse vnwJobSearchResponse = vietnamWorksJobSearchService.searchJob(vnwJobSearchRequest);
    JobSearchResponse jobSearchResponse = convertToJobSearchResponse(vnwJobSearchResponse);
    return jobSearchResponse;
  }


//    @SendTo("/topic/jobs/search")
//    @Consume(uri = "direct:jobs/search/response")
//    @RecipientList
//    public void replySearchJobs(JobSearchResponse jobSearchResponse) {
//        messagingTemplate.convertAndSend("/topic/jobs/search", jobSearchResponse);
//    }


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

  private VNWJobSearchRequest convertToVNWJobSearchRequest(JobSearchRequest jobSearchRequest) {
    VNWJobSearchRequest vnwJobSearchRequest = new VNWJobSearchRequest();
    vnwJobSearchRequest.setJobTitle(jobSearchRequest.getTerms());
    vnwJobSearchRequest.setPageNumber(jobSearchRequest.getPageNumber());
    vnwJobSearchRequest.setJobCategory(category);
    return vnwJobSearchRequest;
  }

  private JobSearchResponse convertToJobSearchResponse(VNWJobSearchResponse vnwJobSearchResponse) {
    JobSearchResponse jobSearchResponse = new JobSearchResponse();
    jobSearchResponse.setTotal(vnwJobSearchResponse.getData().getTotal());
    Set<JobResponse> jobs = new HashSet<JobResponse>();
    JobResponse jobResponse;
    for (VNWJobSearchResponseDataItem item : vnwJobSearchResponse.getData().getJobs()) {
      jobResponse = new JobResponse();
      jobResponse.setUrl(item.getUrl());
      jobResponse.setCompany(item.getCompany());
      jobResponse.setLevel(item.getLevel());
      jobResponse.setLocation(item.getLocation());
      jobResponse.setLogoUrl(item.getLogoUrl());
      jobResponse.setTitle(item.getTitle());
      jobResponse.setPostedOn(item.getPostedOn());
      jobResponse.setVideoUrl(item.getVideoUrl());
      jobs.add(jobResponse);
    }
    jobSearchResponse.setJobs(jobs);
    return jobSearchResponse;
  }
}
