package com.techlooper.controller;

import com.techlooper.model.JobListingCriteria;
import com.techlooper.model.JobListingModel;
import com.techlooper.model.JobResponse;
import com.techlooper.service.JobAlertService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.techlooper.service.impl.JobAlertServiceImpl.NUMBER_OF_ITEMS_PER_PAGE;

@Controller
public class JobListingController {

    @Resource
    private JobAlertService jobAlertService;

    @ResponseBody
    @RequestMapping(value = "/jobListing", method = RequestMethod.POST)
    public JobListingModel list(@RequestBody JobListingCriteria criteria) throws Exception {
        JobListingModel jobListing = new JobListingModel();

        Long totalJob = 0L;
        List<JobResponse> jobs = new ArrayList<>();
        if (StringUtils.isEmpty(criteria.getKeyword()) && StringUtils.isEmpty(criteria.getLocation())) {
            totalJob = jobAlertService.countAllJobs();
            jobs = jobAlertService.listAllJobs(criteria.getPage());
        } else if (StringUtils.isNotEmpty(criteria.getKeyword()) || StringUtils.isNotEmpty(criteria.getLocation())) {
            totalJob = jobAlertService.countJob(criteria);
            jobs = jobAlertService.listJob(criteria);
        }

        Long totalPage = totalJob % NUMBER_OF_ITEMS_PER_PAGE == 0 ?
                totalJob / NUMBER_OF_ITEMS_PER_PAGE : totalJob / NUMBER_OF_ITEMS_PER_PAGE + 1;
        jobListing.setPage(criteria.getPage());
        jobListing.setTotalPage(totalPage);
        jobListing.setTotalJob(totalJob);
        jobListing.setJobs(jobs);
        return jobListing;
    }

}
