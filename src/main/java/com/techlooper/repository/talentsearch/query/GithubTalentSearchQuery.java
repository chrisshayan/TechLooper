package com.techlooper.repository.talentsearch.query;

import com.techlooper.model.TalentSearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

/**
 * Created by NguyenDangKhoa on 3/16/15.
 */
@Service("GITHUBTalentSearchQuery")
public class GithubTalentSearchQuery implements TalentSearchQuery {

    @Override
    public SearchQuery getSearchQuery(TalentSearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!searchRequest.getSkills().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("profiles.GITHUB.skills", searchRequest.getSkills()));
        }
        if (!searchRequest.getLocations().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("profiles.GITHUB.location", searchRequest.getLocations()));
        }
        if (!searchRequest.getCompanies().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("profiles.GITHUB.company", searchRequest.getCompanies()));
        }

        return new NativeSearchQueryBuilder()
                .withQuery(nestedQuery("profiles", boolQueryBuilder))
                .withSort(SortBuilders.fieldSort(searchRequest.getSortByField()).order(SortOrder.DESC))
                .withPageable(new PageRequest(searchRequest.getPageIndex(), searchRequest.getPageSize()))
                .build();
    }

}
