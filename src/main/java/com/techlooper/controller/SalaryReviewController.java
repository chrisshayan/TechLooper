package com.techlooper.controller;

import com.techlooper.entity.JobEntity;
import com.techlooper.model.*;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.SalaryReviewService;
import com.techlooper.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SalaryReviewController {

    @Resource
    private Mapper dozerMapper;

    @Resource
    private SalaryReviewService salaryReviewService;

    @Resource
    private UserService userService;

    @Resource
    private JobSearchService jobSearchService;

    @CrossOrigin
    @RequestMapping(value = "/widget/salaryReview", method = RequestMethod.POST)
    public SalaryReviewResultDto reviewSalaryFromWidget(@RequestBody SalaryReviewDto salaryReviewDto) {
        setSalaryReviewConditionFromJobId(salaryReviewDto);
        SalaryReviewResultDto salaryReviewResult = salaryReviewService.reviewSalary(salaryReviewDto);
        boolean hideSalary = salaryReviewDto.getIsSalaryVisible() != null && !salaryReviewDto.getIsSalaryVisible();
        if (hideSalary) {
            hideSalaryInformation(salaryReviewResult);
        }
        return salaryReviewResult;
    }

    @RequestMapping(value = "/salaryReview", method = RequestMethod.POST)
    public SalaryReviewResultDto reviewSalary(@RequestBody SalaryReviewDto salaryReviewDto) {
        SalaryReviewResultDto salaryReviewResult = salaryReviewService.reviewSalary(salaryReviewDto);

        salaryReviewService.saveSalaryReviewResult(salaryReviewResult);
        // get top 3 higher salary jobs
        List<TopPaidJob> topPaidJobs = salaryReviewService.findTopPaidJob(salaryReviewDto);
        salaryReviewResult.setTopPaidJobs(topPaidJobs);

        SimilarSalaryReviewRequest request = dozerMapper.map(salaryReviewResult, SimilarSalaryReviewRequest.class);
        List<SimilarSalaryReview> similarSalaryReviews = salaryReviewService.getSimilarSalaryReview(request);
        salaryReviewResult.setSimilarSalaryReviews(similarSalaryReviews);

        return salaryReviewResult;
    }

    @RequestMapping(value = "/salaryReview/{salaryReviewId}", method = RequestMethod.GET)
    public SalaryReviewDto getReviewSalary(@PathVariable("salaryReviewId") String salaryReviewId) {
        return userService.findSalaryReviewById(salaryReviewId);
    }

    @RequestMapping(value = "/saveSalaryReviewSurvey", method = RequestMethod.POST)
    public void saveSurvey(@RequestBody SalaryReviewSurvey salaryReviewSurvey, HttpServletResponse httpServletResponse) {
        boolean isSaved = salaryReviewService.saveSalaryReviewSurvey(salaryReviewSurvey);
        if (isSaved) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } else {
            httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    @RequestMapping(value = "salaryReview/placeSalaryReviewReport", method = RequestMethod.POST)
    public void placeSalaryReviewReport(@Valid @RequestBody SalaryReviewEmailRequest emailRequest) {
        salaryReviewService.sendSalaryReviewReportEmail(emailRequest);
    }

    private void setSalaryReviewConditionFromJobId(SalaryReviewDto salaryReviewDto) {
        if (StringUtils.isNotEmpty(salaryReviewDto.getTechlooperJobId())) {
            JobEntity job = jobSearchService.findJobById(salaryReviewDto.getTechlooperJobId());
            if (job != null) {
                salaryReviewDto.setJobTitle(job.getJobTitle());
                salaryReviewDto.setSalaryMin(job.getSalaryMin());
                salaryReviewDto.setSalaryMax(job.getSalaryMax());
                Integer salary = jobSearchService.getAverageSalary(job.getSalaryMin(), job.getSalaryMax()).intValue();
                salaryReviewDto.setNetSalary(salary);
                List<Long> jobCategories = job.getIndustries().stream().map(jobIndustry -> jobIndustry.getIndustryId()).collect(Collectors.toList());
                salaryReviewDto.setJobCategories(jobCategories);
                salaryReviewDto.setIsSalaryVisible(job.getIsSalaryVisible());
            }
        }
    }

    private void hideSalaryInformation(SalaryReviewResultDto salaryReviewResult) {
        salaryReviewResult.setNetSalary(null);
        salaryReviewResult.setSalaryMin(null);
        salaryReviewResult.setSalaryMax(null);
        SalaryReport salaryReport = salaryReviewResult.getSalaryReport();
        if (salaryReport != null) {
            salaryReport.setNetSalary(null);
        }
    }

}
