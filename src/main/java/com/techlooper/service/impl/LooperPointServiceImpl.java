package com.techlooper.service.impl;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.HistogramEnum;
import com.techlooper.repository.userimport.UserImportRepository;
import com.techlooper.service.JobStatisticService;
import com.techlooper.service.LooperPointService;
import com.techlooper.service.UserEvaluationService;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
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
import java.util.Map;

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

  @Resource
  private JobStatisticService jobStatisticService;

  final String[] countries = {"japan", "singapore", "myanmar", "cambodia", "thailand", "malaysia", "indonesia", "australia", "china", "india", "korea", "taiwan",
    "spain", "ukraine", "poland", "russia", "bulgaria", "turkey", "greece", "serbia", "romania", "belarus", "lithuania", "estonia",
    "italy", "portugal", "colombia", "brazil", "chile", "argentina", "venezuela", "bolivia", "mexico"};
//  final String[] countries = {"vietnam"};

  //@Scheduled(cron = "${scheduled.cron.looperpoints}")// start every week at FRI 19:00:00
  public void evaluateCandidates() {
//    Map<String, Long> totalNumberOfJobPerSkill = userEvaluationService.getTotalNumberOfJobPerSkill();
//    Long totalITJobs = jobStatisticService.countTotalITJobsWithinPeriod(HistogramEnum.TWO_QUARTERS);
//    LOGGER.info("Start service to evaluate candidates...");
//    for (String country : countries) {
//      LOGGER.info("Do for contry: {}", country);
//      scoringByCountry(country, totalNumberOfJobPerSkill, totalITJobs);
//    }
//    LOGGER.info("Done job!!!");
  }

  private void scoringByCountry(String country, Map<String, Long> totalNumberOfJobPerSkill, Long totalITJobs) {
//    NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withIndices(indexName)
//      .withTypes("user");//.withPageable();//.withSearchType(SearchType.COUNT);
//    queryBuilder.withQuery(QueryBuilders.nestedQuery("profiles", QueryBuilders.matchQuery("profiles.GITHUB.location", country)))
//            .withFilter(FilterBuilders.notFilter(FilterBuilders.existsFilter("ranks")));
//
//    int total = (int) elasticsearchTemplateUserImport.count(queryBuilder.withSearchType(SearchType.COUNT).build());
//    int maxPageNumber = total % 50 > 0 ? total / 50 + 1 : total / 50 ;
//    for (int pageNumber = 0; pageNumber < maxPageNumber; ++pageNumber) {
//      Instant start = Instant.now();
//      queryBuilder.withSearchType(SearchType.DFS_QUERY_THEN_FETCH).withPageable(new PageRequest(pageNumber, 50));
//      final List<UserImportEntity> users = new ArrayList<>();
//      elasticsearchTemplateUserImport.queryForPage(queryBuilder.build(), UserImportEntity.class)
//        .forEach(userImportEntity -> {
//          LOGGER.debug("Evaluate user {}", userImportEntity.getEmail());
//          userImportEntity.setScore(userEvaluationService.score(userImportEntity, totalNumberOfJobPerSkill));
//          userImportEntity.setRate(userEvaluationService.rate(userImportEntity, totalNumberOfJobPerSkill, totalITJobs));
//          userImportEntity.setRanks(userEvaluationService.rank(userImportEntity));
//          users.add(userImportEntity);
//          LOGGER.info("Done evaluate user {}", userImportEntity.getEmail() + " with score " + userImportEntity.getScore());
//        });
//      Iterable<UserImportEntity> savedUsers = userImportRepository.save(users);
//      Instant end = Instant.now();
//      LOGGER.info("Submitted {} users in time {}", savedUsers, Duration.between(start, end));
//    }
  }

  private FilterBuilder getCandidateByCountry(String country) {
    return FilterBuilders.nestedFilter("profiles", FilterBuilders.termFilter("location", country));
  }
}
