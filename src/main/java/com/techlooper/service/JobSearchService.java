package com.techlooper.service;

import com.techlooper.entity.JobEntity;
import com.techlooper.entity.SalaryReviewEntity;
import com.techlooper.model.VNWConfigurationResponse;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VNWJobSearchResponse;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
public interface JobSearchService {

    /**
     * Loads the configuration of the VietnamWorks search
     *
     * @return See more at {@link com.techlooper.model.VNWConfigurationResponse}
     */
    VNWConfigurationResponse getConfiguration();

    /**
     * This method executes the search VietnamWorks search API
     *
     * @param jobSearchRequest See more at {@link com.techlooper.model.VNWJobSearchRequest}
     * @return Response of the search in format of {@link com.techlooper.model.VNWJobSearchResponse}
     */
    VNWJobSearchResponse searchJob(VNWJobSearchRequest jobSearchRequest);

    List<JobEntity> getJobSearchResult(NativeSearchQueryBuilder queryBuilder);

    List<JobEntity> getHigherSalaryJobs(SalaryReviewEntity salaryReviewEntity);

    Double getAverageSalary(Long salaryMin, Long salaryMax);
}
