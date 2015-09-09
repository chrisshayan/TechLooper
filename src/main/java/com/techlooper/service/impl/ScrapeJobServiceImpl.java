package com.techlooper.service.impl;

import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.KimonoJob;
import com.techlooper.model.KimonoJobList;
import com.techlooper.model.KimonoJobModel;
import com.techlooper.repository.userimport.ScrapeJobRepository;
import com.techlooper.service.ScrapeJobService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.techlooper.util.DateTimeUtils.BASIC_DATE_PATTERN;
import static com.techlooper.util.DateTimeUtils.currentDate;
import static java.util.stream.Collectors.toList;

@Service
public class ScrapeJobServiceImpl implements ScrapeJobService {

    @Resource
    private ScrapeJobRepository scrapeJobRepository;

    @Override
    public void save(final KimonoJobModel jobModel) {
        KimonoJobList jobList = jobModel.getResults();
        String crawlSource = jobModel.getCrawlSource();

        if (jobList != null) {
            List<KimonoJob> jobs = jobList.getJobs();
            if (jobs != null && !jobs.isEmpty()) {
                List<ScrapeJobEntity> jobEntities = jobs.stream().filter(job -> notExist(job)).map(job ->
                        convertToJobEntity(job, crawlSource)).collect(toList());
                scrapeJobRepository.save(jobEntities);
            }
        }
    }

    private ScrapeJobEntity convertToJobEntity(KimonoJob job, String crawlSource) {
        ScrapeJobEntity jobEntity = new ScrapeJobEntity();
        // Let ElasticSearch auto-generate ID for job from KimonoLab
        jobEntity.setJobId(null);
        jobEntity.setJobTitle(job.getJobTitle().getText());
        jobEntity.setJobTitleUrl(job.getJobTitle().getHref());
        jobEntity.setCompany(job.getCompany());
        jobEntity.setSalary(job.getSalary());
        jobEntity.setLocation(job.getLocation());
        jobEntity.setCrawlSource(crawlSource);
        jobEntity.setCreatedDateTime(currentDate(BASIC_DATE_PATTERN));

        if (job.getCompanyLogoUrl() != null) {
            jobEntity.setCompanyLogoUrl(job.getCompanyLogoUrl().getHref());
        }
        return jobEntity;
    }

    private boolean notExist(KimonoJob job) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("job");
        searchQueryBuilder.withQuery(QueryBuilders.matchPhraseQuery("jobTitleUrl", job.getJobTitle().getHref()));
        long totalJob = scrapeJobRepository.search(searchQueryBuilder.build()).getTotalElements();
        return totalJob == 0;
    }

}
