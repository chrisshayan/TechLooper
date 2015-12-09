package com.techlooper.util;

import com.techlooper.entity.ChallengeCriteria;
import com.techlooper.model.Language;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.*;

public class DataUtils {

    public static <T> List<T> getAllEntities(ElasticsearchRepository<T, ?> repository, NativeSearchQueryBuilder searchQueryBuilder) {
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

    public static Set<ChallengeCriteria> defaultChallengeCriterias(Language lang) {
        Set<ChallengeCriteria> defaultCriterias = new HashSet<>();
        if (lang == Language.en) {
            defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
                    .withName("UI/UX implemented solution").withWeight(20L).build());
            defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
                    .withName("Creativity on the proposed solution").withWeight(20L).build());
            defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
                    .withName("Source code quality").withWeight(20L).build());
            defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
                    .withName("Usage of top edge technology").withWeight(20L).build());
            defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
                    .withName("Application functionality").withWeight(20L).build());
        } else {
            defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
                    .withName("Giải pháp lựa chọn cho UI/UX").withWeight(20L).build());
            defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
                    .withName("Mức độ sáng tạo của giải pháp đề xuất").withWeight(20L).build());
            defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
                    .withName("Chất lượng mã nguồn").withWeight(20L).build());
            defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
                    .withName("Sử dụng các công nghệ hàng đầu tiên tiến").withWeight(20L).build());
            defaultCriterias.add(ChallengeCriteria.ChallengeCriteriaBuilder.challengeCriteria()
                    .withName("Chức năng của ứng dụng").withWeight(20L).build());
        }

        return defaultCriterias;
    }

    public static String generateStringId() {
        return UUID.randomUUID().toString();
    }

    public static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();
    }
}
