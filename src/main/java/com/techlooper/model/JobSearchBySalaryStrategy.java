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
public class JobSearchBySalaryStrategy extends JobSearchStrategy {

    private SalaryReviewDto salaryReviewDto;

    private JobQueryBuilder jobQueryBuilder;

    private VietnamworksJobRepository vietnamworksJobRepository;

    public JobSearchBySalaryStrategy(SalaryReviewDto salaryReviewDto, JobQueryBuilder jobQueryBuilder,
                                     VietnamworksJobRepository vietnamworksJobRepository) {
        this.salaryReviewDto = salaryReviewDto;
        this.jobQueryBuilder = jobQueryBuilder;
        this.vietnamworksJobRepository = vietnamworksJobRepository;
    }

    @Override
    protected NativeSearchQueryBuilder getSearchQueryBuilder() {
        return jobQueryBuilder.getJobSearchQueryForSalaryReview(salaryReviewDto);
    }

    @Override
    protected ElasticsearchRepository<JobEntity, ?> getJobRepository() {
        return vietnamworksJobRepository;
    }

}
