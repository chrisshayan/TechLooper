package com.techlooper.repository.talentsearch;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.TalentSearchRequest;
import com.techlooper.service.impl.GithubTalentSearchDataProcessor;
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
public class GithubTalentSearchRepository implements TalentSearchRepository {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplateUserImport;

    @Resource
    private GithubTalentSearchDataProcessor dataProcessor;

    @Override
    public List<UserImportEntity> findTalent(TalentSearchRequest param) {
        dataProcessor.normalizeInputParameter(param);
        final SearchQuery searchQuery = getSearchQuery(param);
        return elasticsearchTemplateUserImport.queryForList(searchQuery, UserImportEntity.class);
    }

    @Override
    public long countTalent(TalentSearchRequest param) {
        final SearchQuery searchQuery = getSearchQuery(param);
        return elasticsearchTemplateUserImport.count(searchQuery, UserImportEntity.class);
    }

    private SearchQuery getSearchQuery(TalentSearchRequest param) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!param.getSkills().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("profiles.GITHUB.skills", param.getSkills()));
        }
        if (!param.getLocations().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("profiles.GITHUB.location", param.getLocations()));
        }
        if (!param.getCompanies().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("profiles.GITHUB.company", param.getCompanies()));
        }

        return new NativeSearchQueryBuilder()
                .withQuery(nestedQuery("profiles", boolQueryBuilder))
                .withSort(SortBuilders.fieldSort(param.getSortByField()).order(SortOrder.DESC))
                .withPageable(new PageRequest(param.getPageIndex(), param.getPageSize()))
                .build();
    }

}
