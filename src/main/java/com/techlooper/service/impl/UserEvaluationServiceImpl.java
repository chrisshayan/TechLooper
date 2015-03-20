package com.techlooper.service.impl;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.HistogramEnum;
import com.techlooper.model.SocialProvider;
import com.techlooper.service.JobStatisticService;
import com.techlooper.service.UserEvaluationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NguyenDangKhoa on 3/19/15.
 */
@Service
public class UserEvaluationServiceImpl implements UserEvaluationService {

    @Resource
    private JobStatisticService jobStatisticService;

    @Override
    public long score(UserImportEntity user) {
        long score = 0L;

        if (StringUtils.isNotEmpty(user.getEmail()) && !user.getEmail().contains("missing.com")) {
            score += 10;
        }

        Map<String, Object> profile = (Map<String, Object>) user.getProfiles().get(SocialProvider.GITHUB);
        Integer numberOfRepos = (Integer) profile.get("numberOfRepositories");
        score += numberOfRepos * 10;

        List<String> skills = (List<String>) profile.get("skills");
        for (String skill : skills) {
            score += jobStatisticService.countJobsBySkillWithinPeriod(skill, HistogramEnum.TWO_QUARTERS);
        }

        return score;
    }

    @Override
    public double rate(UserImportEntity user) {
        long score = 0L;

        Map<String, Object> profile = (Map<String, Object>) user.getProfiles().get(SocialProvider.GITHUB);
        List<String> skills = (List<String>) profile.get("skills");
        for (String skill : skills) {
            score += jobStatisticService.countJobsBySkillWithinPeriod(skill, HistogramEnum.TWO_QUARTERS);
        }

        Long totalITJobs = jobStatisticService.countTotalITJobsWithinPeriod(HistogramEnum.TWO_QUARTERS);
        if (totalITJobs > 0) {
            double rate = score * 5D / jobStatisticService.countTotalITJobsWithinPeriod(HistogramEnum.TWO_QUARTERS);
            if (rate >= 5D) {
                rate = 5D;
            } else {
                rate = Math.ceil(rate * 2) / 2;
            }

            return rate;
        } else {
            return 0D;
        }
    }

    @Override
    public Map<String, Long> rank(UserImportEntity user) {
        return new HashMap<>();
    }
}
