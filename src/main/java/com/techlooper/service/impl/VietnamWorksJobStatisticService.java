package com.techlooper.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobStatisticService;

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

   public Long countProjectManagerJobs() {
      return count(TechnicalTermEnum.PROJECT_MANAGER);
   }

   public Long countBAJobs() {
      return count(TechnicalTermEnum.BA);
   }

   public Long countQAJobs() {
      return count(TechnicalTermEnum.QA);
   }

   public Long countDBAJobs() {
      return count(TechnicalTermEnum.QA);
   }

   public Long countPythonJobs() {
      return count(TechnicalTermEnum.PYTHON);
   }

   public Long countRubyJobs() {
      return count(TechnicalTermEnum.RUBY);
   }

   /**
    * Counts the matching jobs to relevant {@code TechnicalTermEnum}
    * 
    * @param technicalTermEnum
    *           a {@code TechnicalTermEnum} to determine which technology search
    *           must happen.
    * @return a {@code Long} that represents number of matching jobs.
    */
   public Long count(final TechnicalTermEnum technicalTermEnum) {
      final SearchQuery searchQuery = new NativeSearchQueryBuilder()
            .withQuery(
                  multiMatchQuery(technicalTermEnum.toString(), "jobTitle", "jobDescription", "skillExperience")
                        .operator(Operator.AND)).withIndices("vietnamworks").withSearchType(SearchType.COUNT).build();
      return elasticsearchTemplate.count(searchQuery);
   }

   public Long countTechnicalJobs() {
      Long count = 0L;
      for (TechnicalTermEnum term : TechnicalTermEnum.values()) {
         count += count(term);
      }
      return count;
   }
}
