package com.techlooper.repository;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.entity.JobEntity;
import com.techlooper.repository.elasticsearch.JobSearchResultRepository;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by chrisshayan on 7/11/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchConfiguration.class, ElasticsearchUserImportConfiguration.class})
public class JobSearchResultRepositoryITCase {

    @Resource
    private JobSearchResultRepository repository;

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Before
    public void empty() {
        assertNotNull(repository);
        assertNotNull(elasticsearchTemplate);
    }

    @Test
    public void doTest() {
        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withPageable(new PageRequest(0, 10))
                .withTypes("job")
                .build();

        FacetedPage<JobEntity> jobSearchResultEntities = repository.search(searchQuery);
        assertNotNull(jobSearchResultEntities);

        jobSearchResultEntities.forEach(job -> {
            System.out.println("job = " + job);
        });

        final long count = elasticsearchTemplate.count(searchQuery);
        assertThat(count >= 0, Is.is(Boolean.TRUE));
    }


}
