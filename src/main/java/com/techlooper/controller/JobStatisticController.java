package com.techlooper.controller;

import com.techlooper.model.*;
import com.techlooper.service.JobStatisticService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
public class JobStatisticController {

    @Resource
    private JobStatisticService vietnamWorksJobStatisticService;

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    private List<TechnicalTerm> technicalTerms;

    @Scheduled(cron = "${scheduled.cron}")
    public void countTechnicalJobs() {
        technicalTerms.stream().forEach(term ->
                        messagingTemplate.convertAndSend("/topic/jobs/term/" + term.getName(), new JobStatisticResponse.Builder()
                                .withCount(vietnamWorksJobStatisticService.count(term)).build())
        );
    }

    @SendTo("/topic/jobs/terms")
    @MessageMapping("/jobs/terms")
    public List<TechnicalTermResponse> countTechnicalTerms() {
        List<TechnicalTermResponse> terms = new LinkedList<>();
        technicalTerms.stream().forEach(term ->
                        terms.add(new TechnicalTermResponse.Builder().withTerm(term)
                                .withCount(vietnamWorksJobStatisticService.count(term)).build())
        );
        return terms;
    }

    @MessageMapping("/jobs")
    public void countTechnicalJobs(JobStatisticRequest request) {
        messagingTemplate.convertAndSend(
                "/topic/jobs/" + request.getTerm().toLowerCase(),
                new JobStatisticResponse.Builder().withCount(
                        vietnamWorksJobStatisticService.count(convertToTechnicalTerm(request.getTerm().toUpperCase())))
                        .build());
    }

    @SendTo("/topic/analytics/skill")
    @MessageMapping("/analytics/skill")
    public SkillStatisticResponse countTechnicalSkillByTerm(SkillStatisticRequest skillStatisticRequest) {
        return vietnamWorksJobStatisticService.countJobsBySkill(skillStatisticRequest.getTerm(), skillStatisticRequest.getHistograms());
    }

    private TechnicalTerm convertToTechnicalTerm(String termName) {
        return technicalTerms.stream().filter(term -> term.getName().equals(termName)).findFirst().get();
    }
}
