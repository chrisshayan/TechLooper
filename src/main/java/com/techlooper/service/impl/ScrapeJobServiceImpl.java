package com.techlooper.service.impl;

import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.KimonoJob;
import com.techlooper.model.KimonoJobList;
import com.techlooper.model.KimonoJobModel;
import com.techlooper.repository.elasticsearch.ScrapeJobRepository;
import com.techlooper.service.ScrapeJobService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
                List<ScrapeJobEntity> jobEntities = jobs.stream().map(job ->
                        convertToJobEntity(job, crawlSource)).collect(toList());
                scrapeJobRepository.save(jobEntities);
            }
        }
    }

    private ScrapeJobEntity convertToJobEntity(KimonoJob job, String crawlSource) {
        ScrapeJobEntity jobEntity = new ScrapeJobEntity();
        jobEntity.setJobTitle(job.getJobTitle().getText());
        jobEntity.setJobTitleUrl(job.getJobTitle().getHref());
        jobEntity.setCompany(job.getCompany());
        jobEntity.setSalary(job.getSalary());
        jobEntity.setLocation(job.getLocation());
        jobEntity.setCrawlSource(crawlSource);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        jobEntity.setCreatedDateTime(simpleDateFormat.format(new Date()));

        if (job.getCompanyLogoUrl() != null) {
            jobEntity.setCompanyLogoUrl(job.getCompanyLogoUrl().getHref());
        }
        return jobEntity;
    }
}
