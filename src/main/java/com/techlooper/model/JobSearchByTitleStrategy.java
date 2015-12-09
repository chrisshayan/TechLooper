package com.techlooper.model;

import com.techlooper.entity.JobEntity;
import com.techlooper.repository.elasticsearch.VietnamworksJobRepository;
import com.techlooper.service.JobQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by NguyenDangKhoa on 12/7/15.
 */
public class JobSearchByTitleStrategy extends JobSearchStrategy {

    private JobQueryBuilder jobQueryBuilder;

    private String jobTitle;

    private VietnamworksJobRepository vietnamworksJobRepository;

    public JobSearchByTitleStrategy(VietnamworksJobRepository vietnamworksJobRepository, JobQueryBuilder jobQueryBuilder,
                                    String jobTitle) {
        this.jobQueryBuilder = jobQueryBuilder;
        this.jobTitle = jobTitle;
        this.vietnamworksJobRepository = vietnamworksJobRepository;
    }

    @Override
    protected NativeSearchQueryBuilder getSearchQueryBuilder() {
        return jobQueryBuilder.getJobSearchQueryByJobTitle(jobTitle);
    }

    @Override
    protected ElasticsearchRepository<JobEntity, ?> getJobRepository() {
        return vietnamworksJobRepository;
    }

}
