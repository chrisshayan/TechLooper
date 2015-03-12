package com.techlooper.repository.userimport;

import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.TalentSearchParam;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.nestedQuery;

/**
 * Created by NguyenDangKhoa on 3/12/15.
 */
public class GithubTalentSearchRepository implements TalentSearchRepository {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplateUserImport;

    @Override
    public List<UserImportEntity> findTalent(TalentSearchParam param) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //TODO : should implement data pre-processor before search
        param.getSkills().removeAll(Arrays.asList(null, ""));
        param.getLocations().removeAll(Arrays.asList(null, ""));
        param.getCompanies().removeAll(Arrays.asList(null, ""));
        if (!param.getSkills().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("profiles.GITHUB.skills", param.getSkills()));
        }
        if (!param.getLocations().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("profiles.GITHUB.location", param.getLocations()));
        }
        if (!param.getCompanies().isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("profiles.GITHUB.company", param.getCompanies()));
        }

        final SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(nestedQuery("profiles", boolQueryBuilder))
                .withSort(SortBuilders.fieldSort(param.getSortByField()).order(SortOrder.DESC))
                .withPageable(new PageRequest(param.getPageIndex(), param.getPageSize()))
                .build();
        return elasticsearchTemplateUserImport.queryForList(searchQuery, UserImportEntity.class);
    }
}
