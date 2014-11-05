package com.techlooper.controller;

import com.techlooper.model.*;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.JobStatisticService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        final VNWJobSearchRequest vnwJobSearchRequest = convertToVNWJobSearchRequest(searchJobsRequest);
        final VNWJobSearchResponse vnwJobSearchResponse = vietnamWorksJobSearchService.searchJob(vnwJobSearchRequest);
        return convertToJobSearchResponse(vnwJobSearchResponse);
    }


    @Scheduled(cron = "${scheduled.cron}")
    public void countTechnicalJobs() {
        Arrays.stream(TechnicalTermEnum.values()).forEach(term -> {
            messagingTemplate.convertAndSend("/topic/jobs/term/" + term.name(), new JobStatisticResponse.Builder()
                    .withCount(vietnamWorksJobStatisticService.count(term)).build());
        });
    }

    /**
     * need to think a way how fetch TechnicalTerms dynamic, we might rework on
     * business model
     */
    @SendTo("/topic/jobs/terms")
    @MessageMapping("/jobs/terms")
    public List<TechnicalTermResponse> countTechnicalTerms() {
        List<TechnicalTermResponse> terms = new LinkedList<TechnicalTermResponse>();
        Arrays.stream(TechnicalTermEnum.values()).forEach(term -> {
            terms.add(new TechnicalTermResponse.Builder().withTerm(term)
                    .withCount(vietnamWorksJobStatisticService.count(term)).build());
        });
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

    /**
     * need to think a way how fetch TechnicalTerms dynamic, we might rework on
     * business model
     */
    @SendTo("/topic/analytics/skill")
    @MessageMapping("/analytics/skill")
    public SkillStatisticResponse countTechnicalSkillByTerm(SkillStatisticRequest skillStatisticRequest) {
        TechnicalTermEnum term = skillStatisticRequest.getTerm();
        String period = skillStatisticRequest.getPeriod();
        int dayGapOfPeriod = getDayGapOfPeriod(period);
        LocalDate currentPeriod = LocalDate.now();
        LocalDate previousPeriod = LocalDate.now().minusDays(dayGapOfPeriod);

        Long totalJobs = vietnamWorksJobStatisticService.count(term);

        List<SkillStatisticItem> items = new ArrayList<SkillStatisticItem>();
        TechnicalSkillEnumMap.skillOf(term).stream().forEach(skill -> {
            Long currentSkill = vietnamWorksJobStatisticService.countTechnicalJobsBySkill(term, skill, currentPeriod);
            Long previousSkill = vietnamWorksJobStatisticService.countTechnicalJobsBySkill(term, skill, previousPeriod);
            items.add(new SkillStatisticItem(skill, currentSkill, previousSkill));
        });

        return new SkillStatisticResponse(term, totalJobs, period, items);
    }

    private int getDayGapOfPeriod(String period) {
        Calendar calendar = Calendar.getInstance();
        Optional periodOptional = Optional.ofNullable(period);
        if (periodOptional.isPresent()) {
            switch (period) {
                case "quarter":
                    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH) * 3;
                case "month":
                    return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                default:
                    return calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
            }
        } else {
            return calendar.getActualMaximum(Calendar.DAY_OF_WEEK);
        }
    }

    private VNWJobSearchRequest convertToVNWJobSearchRequest(JobSearchRequest jobSearchRequest) {
        return new VNWJobSearchRequest(
                jobSearchRequest.getTerms(),
                category,
                jobSearchRequest.getPageNumber()
        );
    }

    private JobSearchResponse convertToJobSearchResponse(VNWJobSearchResponse vnwJobSearchResponse) {
        final JobSearchResponse jobSearchResponse = new JobSearchResponse();
        jobSearchResponse.setTotal(vnwJobSearchResponse.getData().getTotal());
        final Stream<VNWJobSearchResponseDataItem> responseDataItemStream = vnwJobSearchResponse.getData().getJobs().stream();
        jobSearchResponse.setJobs(responseDataItemStream.map(item -> item.toJobResponse()).collect(Collectors.toSet()));
        return jobSearchResponse;
    }
}
