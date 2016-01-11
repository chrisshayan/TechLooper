package com.techlooper.controller;

import com.techlooper.entity.JobEntity;
import com.techlooper.model.*;
import com.techlooper.repository.JsonConfigRepository;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.JobStatisticService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

import static java.util.stream.Collectors.toList;

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

    @Resource
    private JobSearchService jobSearchService;

    private final static List<Integer> JOB_LEVEL_ALL = Collections.EMPTY_LIST;

    @Scheduled(cron = "${scheduled.cron}")
    public void countTechnicalJobs() {
        jsonConfigRepository.getSkillConfig().stream().forEach(term -> {
                    Map<String, Double> avgSalary = vietnamWorksJobStatisticService.getAverageSalaryBySkill(term, JOB_LEVEL_ALL);
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
            Map<String, Double> avgSalary = vietnamWorksJobStatisticService.getAverageSalaryBySkill(term, JOB_LEVEL_ALL);
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

    @RequestMapping(value = "/getPromoted", method = RequestMethod.POST)
    public GetPromotedResponse getTopDemandedSkills(@RequestBody GetPromotedRequest getPromotedRequest) {
        return jobStatisticService.getTopDemandedSkillsByJobTitle(getPromotedRequest);
    }

    @RequestMapping(value = "/getPromotedWidget", method = RequestMethod.POST)
    public GetPromotedResponse getTopDemandedSkillsWidget(@RequestBody GetPromotedRequest getPromotedRequest) {
        setPriceJobConditionFromJobId(getPromotedRequest);
        return jobStatisticService.getTopDemandedSkillsByJobTitle(getPromotedRequest);
    }

    private void setPriceJobConditionFromJobId(GetPromotedRequest getPromotedRequest) {
        if (StringUtils.isNotEmpty(getPromotedRequest.getTechlooperJobId())) {
            JobEntity job = jobSearchService.findJobById(getPromotedRequest.getTechlooperJobId());
            if (job != null) {
                getPromotedRequest.setJobTitle(job.getJobTitle());
                getPromotedRequest.setJobCategoryIds(job.getIndustries().stream().map(industry -> industry.getIndustryId()).collect(toList()));
                getPromotedRequest.setJobLevelIds(Arrays.asList(job.getJobLevelId().intValue()));
            }
        }
    }

}
