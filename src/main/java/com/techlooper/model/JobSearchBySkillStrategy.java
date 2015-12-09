package com.techlooper.model;

import com.techlooper.entity.JobEntity;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Created by NguyenDangKhoa on 12/7/15.
 */
public class JobSearchBySkillStrategy extends JobSearchStrategy {

    private List<String> skills;

    private List<Long> jobCategories;

    private ElasticsearchRepository<JobEntity, ?> repository;

    public JobSearchBySkillStrategy(ElasticsearchRepository repository, List<String> skills, List<Long> jobCategories) {
        this.skills = skills;
        this.jobCategories = jobCategories;
        this.repository = repository;
    }

    @Override
    protected NativeSearchQueryBuilder getSearchQueryBuilder() {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withTypes("job");

        QueryBuilder skillQueryBuilder = skillQueryBuilder(skills);
        queryBuilder.withQuery(filteredQuery(skillQueryBuilder,
                boolFilter().must(getRangeFilterBuilder("approvedDate", "now-6M/M", "now"))
                        .must(getJobIndustriesFilterBuilder(jobCategories))
                        .must(getSalaryRangeFilterBuilder(MIN_SALARY_ACCEPTABLE, MAX_SALARY_ACCEPTABLE))));
        return queryBuilder;
    }

    @Override
    protected ElasticsearchRepository<JobEntity, ?> getJobRepository() {
        return repository;
    }

    private QueryBuilder skillQueryBuilder(List<String> skills) {
        List<String> analyzedSkills = processSkillsBeforeSearch(skills);
        BoolQueryBuilder skillQueryBuilder = boolQuery();
        for (String skill : analyzedSkills) {
            skillQueryBuilder.should(matchQuery("skills.skillName", skill).minimumShouldMatch("100%"));
        }
        return QueryBuilders.nestedQuery("skills", skillQueryBuilder);
    }

    private List<String> processSkillsBeforeSearch(List<String> skills) {
        List<String> analyzedSkills = new ArrayList<>();
        for (String skill : skills) {
            analyzedSkills.add(skill.toLowerCase());
            analyzedSkills.add(skill.toUpperCase());
            analyzedSkills.add(StringUtils.capitalize(skill));
        }
        return analyzedSkills;
    }

}
