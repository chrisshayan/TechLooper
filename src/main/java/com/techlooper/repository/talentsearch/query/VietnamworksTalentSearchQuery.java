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
@Service("VIETNAMWORKSTalentSearchQuery")
public class VietnamworksTalentSearchQuery implements TalentSearchQuery {

    private static final String[] GENERAL_SEARCH_FIELDS = new String[]{
            "profiles.VIETNAMWORKS.alias",
            "profiles.VIETNAMWORKS.workexperience",
            "profiles.VIETNAMWORKS.skill",
            "profiles.VIETNAMWORKS.desiredJobTitle",
            "profiles.VIETNAMWORKS.mostRecentPosition",
            "profiles.VIETNAMWORKS.mostRecentEmployer"};

    private static final String[] LOCATION_FIELDS = new String[]{"profiles.VIETNAMWORKS.address",
            "profiles.VIETNAMWORKS.cityName"};

    @Override
    public SearchQuery getSearchQuery(TalentSearchRequest searchRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!searchRequest.getKeywords().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(searchRequest.getKeywords(), GENERAL_SEARCH_FIELDS));
        }
        if (!searchRequest.getLocations().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(searchRequest.getLocations(), LOCATION_FIELDS));
        }

        return new NativeSearchQueryBuilder()
                .withQuery(nestedQuery("profiles", boolQueryBuilder))
                .withSort(SortBuilders.fieldSort(searchRequest.getSortByField()).order(SortOrder.DESC))
                .withPageable(new PageRequest(searchRequest.getPageIndex(), searchRequest.getPageSize()))
                .build();
    }
}
