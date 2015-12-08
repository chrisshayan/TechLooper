package com.techlooper.model;

import com.techlooper.entity.JobEntity;
import com.techlooper.repository.elasticsearch.VietnamworksJobRepository;
import com.techlooper.service.JobQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 12/7/15.
 */
public class JobSearchBySkillStrategy extends JobSearchStrategy {

    private JobQueryBuilder jobQueryBuilder;

    private List<String> skills;

    private List<Long> jobCategories;

    private VietnamworksJobRepository vietnamworksJobRepository;

    public JobSearchBySkillStrategy(VietnamworksJobRepository vietnamworksJobRepository, JobQueryBuilder jobQueryBuilder,
                                    List<String> skills, List<Long> jobCategories) {
        this.jobQueryBuilder = jobQueryBuilder;
        this.skills = skills;
        this.jobCategories = jobCategories;
        this.vietnamworksJobRepository = vietnamworksJobRepository;
    }

    @Override
    protected NativeSearchQueryBuilder getSearchQueryBuilder() {
        return jobQueryBuilder.getJobSearchQueryBySkill(skills, jobCategories);
    }

    @Override
    protected ElasticsearchRepository<JobEntity, ?> getJobRepository() {
        return vietnamworksJobRepository;
    }

}
