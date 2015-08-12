package com.techlooper.service.impl;

import com.techlooper.entity.JobAlertRegistrationEntity;
import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertRegistration;
import com.techlooper.repository.userimport.JobAlertRegistrationRepository;
import com.techlooper.repository.userimport.ScrapeJobRepository;
import com.techlooper.service.JobAlertService;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;

@Service
public class JobAlertServiceImpl implements JobAlertService {

    @Resource
    private ScrapeJobRepository scrapeJobRepository;

    @Resource
    private JobAlertRegistrationRepository jobAlertRegistrationRepository;

    @Resource
    private Mapper dozerMapper;

    @Override
    public List<ScrapeJobEntity> searchJob(JobAlertRegistration jobAlertRegistration) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("job");
        BoolQueryBuilder queryBuilder = boolQuery();

        if (StringUtils.isNotEmpty(jobAlertRegistration.getKeyword())) {
            queryBuilder.must(queryString(jobAlertRegistration.getKeyword()));
        }

        if (StringUtils.isNotEmpty(jobAlertRegistration.getLocation())) {
            queryBuilder.must(matchQuery("location", jobAlertRegistration.getLocation()));
        }

        queryBuilder.must(rangeQuery("createdDateTime").from("now-7d/d"));
        searchQueryBuilder.withQuery(queryBuilder);
        searchQueryBuilder.withSort(fieldSort("createdDateTime").order(SortOrder.DESC));

        return scrapeJobRepository.search(searchQueryBuilder.build()).getContent();
    }

    @Override
    public JobAlertRegistrationEntity registerJobAlert(JobAlertRegistration jobAlertRegistration) {
        JobAlertRegistrationEntity jobAlertRegistrationEntity =
                dozerMapper.map(jobAlertRegistration, JobAlertRegistrationEntity.class);

        Date currentDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        jobAlertRegistrationEntity.setJobAlertRegistrationId(currentDate.getTime());
        jobAlertRegistrationEntity.setCreatedDate(simpleDateFormat.format(currentDate));
        return jobAlertRegistrationRepository.save(jobAlertRegistrationEntity);
    }

    @Override
    public List<JobAlertRegistrationEntity> searchJobAlertRegistration(int period) {
        return null;
    }
}
