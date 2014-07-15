package com.techlooper.service.impl;

import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobStatisticService;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by chrisshayan on 7/14/14.
 */
@Service
public class VietnamWorksJobStatisticService implements JobStatisticService {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    public Long countPhpJobs() {
        return count(TechnicalTermEnum.PHP);
    }

    public Long countJavaJobs() {
        return count(TechnicalTermEnum.JAVA);
    }

    public Long countDotNetJobs() {
        return count(TechnicalTermEnum.DOTNET);
    }

    /**
     * Counts the matching jobs to relevant {@code TechnicalTermEnum}
     * @param technicalTermEnum a {@code TechnicalTermEnum} to determine which technology search must happen.
     * @return a {@code Long} that represents number of matching jobs.
     */
    public Long count(final TechnicalTermEnum technicalTermEnum) {
        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery(
                        technicalTermEnum,
                        "jobTitle",
                        "jobDescription",
                        "skillExperience"
                ))
                .withTypes("job")
                .withIndices("vietnamworks")
                .build();

        return elasticsearchTemplate.count(searchQuery);
    }

}
