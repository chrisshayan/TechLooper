package com.techlooper.controller;

import com.techlooper.model.JobListingCriteria;
import com.techlooper.model.JobListingModel;
import com.techlooper.model.JobResponse;
import com.techlooper.service.JobAggregatorService;
import com.techlooper.service.impl.JobAggregatorServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class JobListingController {

    public static final int NUMBER_OF_ITEMS_PER_PAGE = 10;

    @Resource
    private JobAggregatorService jobAggregatorService;

    @ResponseBody
    @RequestMapping(value = "/jobListing", method = RequestMethod.POST)
    public JobListingModel list(@RequestBody JobListingCriteria criteria) throws Exception {
        JobListingModel jobListing = new JobListingModel();
        Long totalJob = jobAggregatorService.countJob(criteria);
        List<JobResponse> jobs = jobAggregatorService.listJob(criteria);

        Long totalPage = totalJob % NUMBER_OF_ITEMS_PER_PAGE == 0 ?
                totalJob / JobAggregatorServiceImpl.NUMBER_OF_ITEMS_PER_PAGE : totalJob / JobAggregatorServiceImpl.NUMBER_OF_ITEMS_PER_PAGE + 1;
        jobListing.setPage(criteria.getPage());
        jobListing.setTotalPage(totalPage);
        jobListing.setTotalJob(totalJob);
        jobListing.setJobs(jobs);
        return jobListing;
    }

}
