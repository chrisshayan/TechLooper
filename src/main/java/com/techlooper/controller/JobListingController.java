package com.techlooper.controller;

import com.techlooper.model.*;
import com.techlooper.service.JobAlertService;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.ScrapeJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

import static com.techlooper.service.impl.JobAlertServiceImpl.NUMBER_OF_ITEMS_PER_PAGE;

@Controller
public class JobListingController {

  private final static String JOB_CATEGORY_IT = "35,55,57";

  @Resource
  private JobAlertService jobAlertService;

  @Resource
  private ScrapeJobService scrapeJobService;

  @Resource
  private JobSearchService vietnamWorksJobSearchService;

  private final static Logger LOGGER = LoggerFactory.getLogger(JobListingController.class);

  @ResponseBody
  @RequestMapping(value = "/jobListing", method = RequestMethod.POST)
  public JobListingModel list(@RequestBody JobListingCriteria criteria) throws Exception {
    JobListingModel jobListing = new JobListingModel();
    Long totalJob = jobAlertService.countJob(criteria);
    List<JobResponse> jobs = jobAlertService.listJob(criteria);

    Long totalPage = totalJob % NUMBER_OF_ITEMS_PER_PAGE == 0 ?
      totalJob / NUMBER_OF_ITEMS_PER_PAGE : totalJob / NUMBER_OF_ITEMS_PER_PAGE + 1;
    jobListing.setPage(criteria.getPage());
    jobListing.setTotalPage(totalPage);
    jobListing.setTotalJob(totalJob);
    jobListing.setJobs(jobs);
    return jobListing;
  }

  @Scheduled(cron = "${scheduled.cron.indexVietnamworksJob}")
  @RequestMapping(value = "/indexJobFromVietnamworks", method = RequestMethod.GET)
  public void indexJobFromVietnamworks() throws Exception {
    LOGGER.info("START doing index job from vnw");
    VNWJobSearchRequest vnwJobSearchRequest = getTopPriorityJobSearchRequest();
    VNWJobSearchResponse vnwJobSearchResponse;
    do {
      vnwJobSearchResponse = vietnamWorksJobSearchService.searchJob(vnwJobSearchRequest);
      if (vnwJobSearchResponse.hasData()) {
        scrapeJobService.save(vnwJobSearchResponse.getData().getJobs(), Boolean.TRUE);
        vnwJobSearchRequest.setPageNumber(vnwJobSearchRequest.getPageNumber() + 1);
      }
    } while (vnwJobSearchResponse.hasData());

    VNWJobSearchRequest vnwNormalJobSearchRequest = getNormalJobSearchRequest();
    VNWJobSearchResponse vnwNormalJobSearchResponse;
    do {
      vnwNormalJobSearchResponse = vietnamWorksJobSearchService.searchJob(vnwNormalJobSearchRequest);
      if (vnwNormalJobSearchResponse.hasData()) {
        scrapeJobService.save(vnwNormalJobSearchResponse.getData().getJobs(), null);
        vnwNormalJobSearchRequest.setPageNumber(vnwNormalJobSearchRequest.getPageNumber() + 1);
      }
    } while (vnwNormalJobSearchResponse.hasData());

    LOGGER.info("DONE doing index job from vnw!!!");
  }

  private VNWJobSearchRequest getTopPriorityJobSearchRequest() {
    VNWJobSearchRequest vnwJobSearchRequest = new VNWJobSearchRequest();
    vnwJobSearchRequest.setJobCategories(JOB_CATEGORY_IT);
    vnwJobSearchRequest.setTechlooperJobType(1);
    vnwJobSearchRequest.setPageNumber(1);
    vnwJobSearchRequest.setPageSize(20);
    return vnwJobSearchRequest;
  }

  private VNWJobSearchRequest getNormalJobSearchRequest() {
    VNWJobSearchRequest vnwJobSearchRequest = new VNWJobSearchRequest();
    vnwJobSearchRequest.setJobCategories(JOB_CATEGORY_IT);
    vnwJobSearchRequest.setTechlooperJobType(2);
    vnwJobSearchRequest.setPageNumber(1);
    vnwJobSearchRequest.setPageSize(20);
    return vnwJobSearchRequest;
  }

}
