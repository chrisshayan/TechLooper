package com.techlooper.cron;

import com.techlooper.model.JobCrawlerSourceEnum;
import com.techlooper.model.JobSearchCriteria;
import com.techlooper.model.JobTypeEnum;
import com.techlooper.service.JobAggregatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * This is cron job class that index new top priority and normal jobs from Vietnamworks API daily
 *
 * @author Khoa Nguyen
 * @version v0.0-beta9.Release39, 09/09/2015
 */
@Service
public class VietnamworksJobImporter {

    private final static Logger LOGGER = LoggerFactory.getLogger(VietnamworksJobImporter.class);

    @Resource
    private JobAggregatorService jobAggregatorService;

    /**
     * Call Vietnamworks API to fetch new jobs, then import into Techlooper ElasticSearch
     *
     * @throws Exception
     */
    @Scheduled(cron = "${scheduled.cron.indexVietnamworksJob}")
    public void indexJobFromVietnamworks() throws Exception {
        try {
            int numberOfTopPriorityJobs = jobAggregatorService.importVietnamworksJob(JobTypeEnum.TOP_PRIORITY);
            int numberOfNormalJobs = jobAggregatorService.importVietnamworksJob(JobTypeEnum.NORMAL);
            LOGGER.info(numberOfTopPriorityJobs + " top priority jobs has been indexed already.");
            LOGGER.info(numberOfNormalJobs + " normal jobs has been indexed already.");

            JobSearchCriteria criteria = new JobSearchCriteria();
            criteria.setCrawlSource(JobCrawlerSourceEnum.VIETNAMWORKS.getValue());
            jobAggregatorService.updateJobExpiration(criteria);
        } catch (Throwable ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

}
