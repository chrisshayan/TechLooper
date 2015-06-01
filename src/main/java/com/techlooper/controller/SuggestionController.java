package com.techlooper.controller;

import com.techlooper.model.SuggestionItem;
import com.techlooper.model.SuggestionResponse;
import com.techlooper.service.SuggestionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SuggestionController {

    @Resource
    private SuggestionService suggestionService;

    @RequestMapping(value = "/suggestion/skill/{query}", method = RequestMethod.GET)
    public SuggestionResponse suggestSkill(@PathVariable String query) {
        SuggestionResponse response = new SuggestionResponse();
        List<SuggestionItem> items = suggestionService.suggestSkills(query).stream()
                .map(SuggestionItem::new).collect(Collectors.toList());
        response.setItems(items);
        return response;
    }

    @RequestMapping(value = "/suggestion/jobTitle/{query}", method = RequestMethod.GET)
    public SuggestionResponse suggestJobTitle(@PathVariable String query) {
        SuggestionResponse response = new SuggestionResponse();
        List<SuggestionItem> items = suggestionService.suggestJobTitles(query).stream()
                .map(SuggestionItem::new).collect(Collectors.toList());
        response.setItems(items);
        return response;
    }
}
