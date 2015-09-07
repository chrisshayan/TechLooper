package com.techlooper.cron;

import com.techlooper.service.JobAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Khoa Nguyen on 07/09/15.
 */
@Service
public class VietnamworksJobImporter {

    private static final int JOB_TYPE_TOP_PRIORITY = 1;

    private static final int JOB_TYPE_NORMAL = 2;

    private final static Logger LOGGER = LoggerFactory.getLogger(VietnamworksJobImporter.class);

    @Resource
    private JobAlertService jobAlertService;

    @Scheduled(cron = "${scheduled.cron.indexVietnamworksJob}")
    public void indexJobFromVietnamworks() throws Exception {
        LOGGER.info("START Vietnamworks Job Importer");
        try {
            int numberOfTopPriorityJobs = jobAlertService.importVietnamworksJob(JOB_TYPE_TOP_PRIORITY);
            int numberOfNormalJobs = jobAlertService.importVietnamworksJob(JOB_TYPE_NORMAL);
            LOGGER.info(numberOfTopPriorityJobs + " top priority jobs has been indexed already.");
            LOGGER.info(numberOfNormalJobs + " normal jobs has been indexed already.");
        } catch (Throwable ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LOGGER.info("DONE Vietnamworks Job Importer");
    }

}
