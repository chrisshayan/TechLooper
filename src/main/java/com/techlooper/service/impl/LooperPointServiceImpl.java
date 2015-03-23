package com.techlooper.service.impl;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.repository.userimport.UserImportRepository;
import com.techlooper.service.LooperPointService;
import com.techlooper.service.UserEvaluationService;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.bootstrap.Elasticsearch;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phuonghqh on 3/20/15.
 */
@Service
public class LooperPointServiceImpl implements LooperPointService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LooperPointServiceImpl.class);

  @Resource(name = "elasticsearchTemplateUserImport")
  private ElasticsearchTemplate elasticsearchTemplateUserImport;

  @Resource
  private UserImportRepository userImportRepository;

  @Value("${elasticsearch.userimport.index.name}")
  private String indexName;

  @Resource
  private UserEvaluationService userEvaluationService;

//  final String[] countries = {"vietnam", "japan", "thailand", "singapore", "malaysia", "indonesia", "australia", "china", "india", "korea", "taiwan",
//    "spain", "ukraine", "poland", "russia", "bulgaria", "turkey", "greece", "serbia", "romania", "belarus", "lithuania", "estonia",
//    "italy", "portugal", "colombia", "brazil", "chile", "argentina", "venezuela", "bolivia", "mexico"};
  final String[] countries = {"vietnam"};

  @Scheduled(cron = "0 00 12 * * MON")// start every week at FRI 19:00:00
  public void evaluateCandidates() {
    LOGGER.info("Start service to evaluate candidates...");
    for (String country : countries) {
      LOGGER.info("Do for contry: {}", country);
      scoringByCountry(country);
    }
    LOGGER.info("Done job!!!");
  }

  private void scoringByCountry(String country) {
    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(indexName)
      .withTypes("user");//.withPageable();//.withSearchType(SearchType.COUNT);
    queryBuilder.withFilter(getCandidateByCountry(country));

    int total = (int) elasticsearchTemplateUserImport.count(queryBuilder.withSearchType(SearchType.COUNT).build());
    int maxPageNumber = total / 50;
    for (int pageNumber = 0; pageNumber < maxPageNumber; ++pageNumber) {
      Instant start = Instant.now();
      queryBuilder.withSearchType(SearchType.DFS_QUERY_THEN_FETCH).withPageable(new PageRequest(pageNumber, 50));
      final List<UserImportEntity> users = new ArrayList<>();
      elasticsearchTemplateUserImport.queryForPage(queryBuilder.build(), UserImportEntity.class)
        .forEach(userImportEntity -> {
          LOGGER.debug("Evaluate user {}", userImportEntity.getEmail());
          userImportEntity.setRate(userEvaluationService.rate(userImportEntity));
          userImportEntity.setScore(userEvaluationService.score(userImportEntity));
          userImportEntity.setRanks(userEvaluationService.rank(userImportEntity));
          users.add(userImportEntity);
          LOGGER.debug("Done evaluate user {}", userImportEntity.getEmail());
        });
      Iterable<UserImportEntity> savedUsers = userImportRepository.save(users);
      Instant end = Instant.now();
      LOGGER.info("Submitted {} users in time {}", savedUsers, Duration.between(start, end));
    }
  }

  private FilterBuilder getCandidateByCountry(String country) {
    return FilterBuilders.nestedFilter("profiles", FilterBuilders.termFilter("location", country));
  }
}
