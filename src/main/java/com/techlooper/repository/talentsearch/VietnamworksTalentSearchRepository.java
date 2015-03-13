package com.techlooper.repository.talentsearch;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.TalentSearchRequest;
import com.techlooper.service.impl.VietnamworksTalentSearchDataProcessor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.annotation.Resource;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

/**
 * Created by NguyenDangKhoa on 3/12/15.
 */
public class VietnamworksTalentSearchRepository implements TalentSearchRepository {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplateUserImport;

    @Resource
    private VietnamworksTalentSearchDataProcessor dataProcessor;

    private static final String[] SKILL_FIELDS = new String[]{"profiles.VIETNAMWORKS.alias",
            "profiles.VIETNAMWORKS.workexperience",
            "profiles.VIETNAMWORKS.skill"};

    private static final String[] JOB_TITLE_FIELDS = new String[]{"profiles.VIETNAMWORKS.desiredJobTitle",
            "profiles.VIETNAMWORKS.mostRecentPosition"};

    private static final String[] COMPANY_FIELDS = new String[]{"profiles.VIETNAMWORKS.mostRecentEmployer"};

    private static final String[] LOCATION_FIELDS = new String[]{"profiles.VIETNAMWORKS.address",
            "profiles.VIETNAMWORKS.cityName"};

    @Override
    public List<UserImportEntity> findTalent(TalentSearchRequest param) {
        dataProcessor.normalizeInputParameter(param);
        final SearchQuery searchQuery = getSearchQuery(param);
        return elasticsearchTemplateUserImport.queryForList(searchQuery, UserImportEntity.class);
    }

    @Override
    public long countTalent(TalentSearchRequest param) {
        dataProcessor.normalizeInputParameter(param);
        final SearchQuery searchQuery = getSearchQuery(param);
        return elasticsearchTemplateUserImport.count(searchQuery, UserImportEntity.class);
    }

    private SearchQuery getSearchQuery(TalentSearchRequest param) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!param.getSkills().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(param.getSkills(), SKILL_FIELDS));
        }
        if (!param.getLocations().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(param.getLocations(), LOCATION_FIELDS));
        }
        if (!param.getCompanies().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.multiMatchQuery(param.getCompanies(), COMPANY_FIELDS));
        }

        return new NativeSearchQueryBuilder()
                .withQuery(nestedQuery("profiles", boolQueryBuilder))
                .withSort(SortBuilders.fieldSort(param.getSortByField()).order(SortOrder.DESC))
                .withPageable(new PageRequest(param.getPageIndex(), param.getPageSize()))
                .build();
    }

}
