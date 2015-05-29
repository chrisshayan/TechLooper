package com.techlooper.service.impl;

import com.techlooper.service.SkillSuggestionService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by NguyenDangKhoa on 5/29/15.
 */
@Service
public class SkillSuggestionServiceImpl implements SkillSuggestionService {

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
            skills = entry.getOptions().stream().map(option -> option.getText().string()).collect(Collectors.toList());
        }

        return skills;
    }

}
