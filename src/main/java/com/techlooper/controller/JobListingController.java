package com.techlooper.controller;

import com.techlooper.model.JobSearchCriteria;
import com.techlooper.model.JobSearchResponse;
import com.techlooper.service.JobAggregatorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Job Listing Controller. It searches the job matching user's criteria
 *
 * @author Khoa Nguyen
 * @version v0.0-beta9.Release39, 09/09/2015
 */
@Controller
public class JobListingController {

    @Resource
    private JobAggregatorService jobAggregatorService;

    /**
     * @param criteria job search criteria
     * @return jobSearchResponse job search response matching search criteria
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/jobListing", method = RequestMethod.POST)
    public JobSearchResponse list(@RequestBody JobSearchCriteria criteria) throws Exception {
        return jobAggregatorService.findJob(criteria);
    }

    /**
     * @param jobAlertRegistrationId job alert registration id
     * @return jobSearchCriteria job alert registration information
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/jobAlertCriteria/{jobAlertRegistrationId}", method = RequestMethod.GET)
    public JobSearchCriteria getJobAlertCriteria(@PathVariable Long jobAlertRegistrationId) throws Exception {
        return jobAggregatorService.findJobAlertCriteriaById(jobAlertRegistrationId);
    }

}
