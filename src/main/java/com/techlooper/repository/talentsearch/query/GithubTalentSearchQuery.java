package com.techlooper.repository.talentsearch.query;

import com.techlooper.model.TalentSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
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

    private static final String[] GENERAL_SEARCH_FIELDS = new String[]{
            "profiles.GITHUB.skills",
            "profiles.GITHUB.company"};

    private static final String LOCATION_SEARCH_FIELDS = "profiles.GITHUB.location";

    @Override
    public SearchQuery getSearchQuery(TalentSearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!searchRequest.getKeywords().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(searchRequest.getKeywords(), GENERAL_SEARCH_FIELDS));
        }
        if (!searchRequest.getLocations().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(LOCATION_SEARCH_FIELDS, searchRequest.getLocations()));
        }

        return new NativeSearchQueryBuilder()
                .withQuery(nestedQuery("profiles", boolQueryBuilder))
                .withSort(SortBuilders.fieldSort(searchRequest.getSortByField()).order(SortOrder.DESC))
                .withPageable(new PageRequest(searchRequest.getPageIndex(), searchRequest.getPageSize()))
                .build();
    }

    public SearchQuery getSearchBySkillQuery(String skill, String sortField) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(skill)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("profiles.GITHUB.skills", skill));
        }

        return new NativeSearchQueryBuilder()
                .withQuery(nestedQuery("profiles", boolQueryBuilder))
                .withSort(SortBuilders.fieldSort(sortField).order(SortOrder.DESC))
                .withPageable(new PageRequest(0, 100000))
                .withIndices("techlooper")
                .build();
    }

    public SearchQuery getSkillStatsQuery() {
        return new NativeSearchQueryBuilder()
                .addAggregation(AggregationBuilders.nested("skill_list").path("profiles")
                    .subAggregation(AggregationBuilders.terms("skill_list").field("profiles.GITHUB.skills").size(0)))
                    .withIndices("techlooper")
                    .build();
    }
}
