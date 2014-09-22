package com.techlooper.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.techlooper.model.JobStatistic;
import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobStatisticService;

@RestController
public class VietnamworksJobStatisticController {

   @Resource(name = "vietnamWorksJobStatisticService")
   private JobStatisticService vietnamWorksJobStatisticService;

   @RequestMapping("/technical-job")
   @ResponseBody
   public List<JobStatistic> countTechnicalJobs() {
      List<JobStatistic> result = new ArrayList<JobStatistic>();
      for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
         result.add(new JobStatistic.Builder().withTerm(term).withCount(vietnamWorksJobStatisticService.count(term))
               .build());
      }
      return result;
   }
}
