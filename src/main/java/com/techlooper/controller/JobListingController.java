package com.techlooper.controller;

import com.techlooper.model.JobSearchCriteria;
import com.techlooper.model.JobSearchResponse;
import com.techlooper.service.JobAggregatorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class JobListingController {
    
    @Resource
    private JobAggregatorService jobAggregatorService;

    @ResponseBody
    @RequestMapping(value = "/jobListing", method = RequestMethod.POST)
    public JobSearchResponse list(@RequestBody JobSearchCriteria criteria) throws Exception {
        return jobAggregatorService.listJob(criteria);
    }

}
