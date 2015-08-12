package com.techlooper.service.impl;

import com.techlooper.entity.ScrapeJobEntity;
import com.techlooper.model.JobAlertCriteria;
import com.techlooper.repository.userimport.ScrapeJobRepository;
import com.techlooper.service.JobAlertService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;

@Service
public class JobAlertServiceImpl implements JobAlertService {

    @Resource
    private ScrapeJobRepository scrapeJobRepository;

    @Override
    public List<ScrapeJobEntity> searchJob(JobAlertCriteria jobAlertCriteria) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("job");
        BoolQueryBuilder queryBuilder = boolQuery();

        if (StringUtils.isNotEmpty(jobAlertCriteria.getKeyword())) {
            queryBuilder.must(queryString(jobAlertCriteria.getKeyword()));
        }

        if (StringUtils.isNotEmpty(jobAlertCriteria.getLocation())) {
            queryBuilder.must(matchQuery("location", jobAlertCriteria.getLocation()));
        }

        queryBuilder.must(rangeQuery("createdDateTime").from("now-7d/d"));
        searchQueryBuilder.withQuery(queryBuilder);
        searchQueryBuilder.withSort(fieldSort("createdDateTime").order(SortOrder.DESC));

        return scrapeJobRepository.search(searchQueryBuilder.build()).getContent();
    }

}
