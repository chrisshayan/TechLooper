package com.techlooper.util;

import com.techlooper.entity.ChallengeCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by NguyenDangKhoa on 10/9/15.
 */
public class DataUtils {

  public static <T> List<T> getAllEntities(ElasticsearchRepository<T, Long> repository, NativeSearchQueryBuilder searchQueryBuilder) {
    List<T> result = new ArrayList<>();

    FacetedPage<T> facetedPage = repository.search(searchQueryBuilder.build());
    int totalPages = facetedPage.getTotalPages();
    int pageIndex = 0;

    while (pageIndex < totalPages) {
      searchQueryBuilder.withPageable(new PageRequest(pageIndex, 50));
      result.addAll(repository.search(searchQueryBuilder.build()).getContent());
      pageIndex++;
    }

    return result;
  }

  public static Set<ChallengeCriteria> defaultChallengeCriterias() {
    Set<ChallengeCriteria> defaultCriterias = new HashSet<>();
    defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withName("UI/UX implemented solution").withWeight(25L).build());
    defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withName("Creativity on the proposed solution").withWeight(25L).build());
    defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withName("Source code quality").withWeight(25L).build());
    defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withName("Usage of top edge technology").withWeight(25L).build());
    defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
      .withName("Application functionality").withWeight(25L).build());
    return defaultCriterias;
  }
}
