package com.techlooper.service.impl;

import com.techlooper.service.SuggestionService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.strip;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Created by NguyenDangKhoa on 5/29/15.
 */
@Service
public class SuggestionServiceImpl implements SuggestionService {

    @Resource
    private TransportClient transportClient;

    @Override
    public List<String> suggestSkills(String query) {
        List<String> skills = new ArrayList<>();
        CompletionSuggestionBuilder skillNameSuggest = new CompletionSuggestionBuilder("skillNameSuggest");
        skillNameSuggest.text(query);
        skillNameSuggest.field("skillNameSuggest");

        SearchResponse searchResponse = transportClient.prepareSearch("suggester").setTypes("skill")
                .setQuery(QueryBuilders.matchAllQuery())
                .addSuggestion(skillNameSuggest).execute().actionGet();

        CompletionSuggestion skilCompletionSuggestion = searchResponse.getSuggest().getSuggestion("skillNameSuggest");
        List<CompletionSuggestion.Entry> entries = skilCompletionSuggestion.getEntries();
        if (!entries.isEmpty()) {
            CompletionSuggestion.Entry entry = entries.get(0);
            skills = entry.getOptions().stream().map(processString()).distinct().collect(Collectors.toList());
        }

        return skills;
    }

    @Override
    public List<String> suggestJobTitles(String query) {
        List<String> jobTitles = new ArrayList<>();
        CompletionSuggestionBuilder jobTitleNameSuggest = new CompletionSuggestionBuilder("jobTitleNameSuggest");
        jobTitleNameSuggest.text(query);
        jobTitleNameSuggest.field("jobTitleNameSuggest");

        SearchResponse searchResponse = transportClient.prepareSearch("suggester").setTypes("jobTitle")
                .setQuery(QueryBuilders.matchAllQuery())
                .addSuggestion(jobTitleNameSuggest).execute().actionGet();

        CompletionSuggestion jobTitleNameCompletionSuggestion = searchResponse.getSuggest().getSuggestion("jobTitleNameSuggest");
        List<CompletionSuggestion.Entry> entries = jobTitleNameCompletionSuggestion.getEntries();
        if (!entries.isEmpty()) {
            CompletionSuggestion.Entry entry = entries.get(0);
            jobTitles = entry.getOptions().stream().map(processString()).distinct().collect(Collectors.toList());
        }

        return jobTitles;
    }

    @Override
    public List<String> searchJobTitles(String query) {
        List<String> jobTitles = new ArrayList<>();

        SearchResponse searchResponse = transportClient.prepareSearch("suggester").setTypes("jobTitle")
                .setQuery(QueryBuilders.matchQuery("jobTitleName", query))
                .setFrom(0).setSize(5).setExplain(true)
                .execute().actionGet();

        if (searchResponse != null && searchResponse.getHits() != null) {
            SearchHit[] hits = searchResponse.getHits().getHits();
            if (hits != null && hits.length > 0) {
                return getTheBestMatchJobTitle(hits);
            }
        }

        return jobTitles;
    }

    private List<String> getTheBestMatchJobTitle(SearchHit[] hits) {
        List<String> jobTitles = new ArrayList<>();
        float maxScore = 0;
        for (SearchHit hit : hits) {
            if (hit.getScore() >= maxScore) {
                maxScore = hit.getScore();
                String jobTitle = hit.getId();
                if (StringUtils.isNotEmpty(jobTitle)) {
                    jobTitles.add(jobTitle);
                }
            } else {
                break;
            }
        }
        return jobTitles;
    }

    private Function<CompletionSuggestion.Entry.Option, String> processString() {
        return option -> {
            return strip(trim(option.getText().string()), ",");
        };
    }

}
