package com.techlooper.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.ArrayList;
import java.util.List;

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
}
