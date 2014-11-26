package com.techlooper.controller;

import com.techlooper.model.*;
import com.techlooper.repository.TechnicalTermRepository;
import com.techlooper.service.JobStatisticService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Controller
public class JobStatisticController {

    @Resource
    private JobStatisticService vietnamWorksJobStatisticService;

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    private TechnicalTermRepository technicalTermRepository;

    @Scheduled(cron = "${scheduled.cron}")
    public void countTechnicalJobs() {
        technicalTermRepository.findAll().stream().forEach(term ->
                        messagingTemplate.convertAndSend("/topic/jobs/term/" + term.getKey(), new JobStatisticResponse.Builder()
                                .withCount(vietnamWorksJobStatisticService.count(term)).build())
        );
    }

    @SendTo("/topic/jobs/terms")
    @MessageMapping("/jobs/terms")
    public List<TechnicalTermResponse> countTechnicalTerms() {
        List<TechnicalTermResponse> terms = new LinkedList<>();
        technicalTermRepository.findAll().stream().forEach(term ->
            terms.add(new TechnicalTermResponse.Builder().withTerm(term.getKey()).withLabel(term.getLabel())
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
        return vietnamWorksJobStatisticService.countJobsBySkill(convertToTechnicalTerm(skillStatisticRequest.getTerm()),
                skillStatisticRequest.getHistograms());
    }

    private TechnicalTerm convertToTechnicalTerm(String termKey) {
        return technicalTermRepository.findAll().stream().filter(term -> term.getKey().equals(termKey)).findFirst().get();
    }
}
