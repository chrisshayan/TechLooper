package com.techlooper.controller;

import com.techlooper.model.SuggestionJobTitleItem;
import com.techlooper.model.SuggestionJobTitleModel;
import com.techlooper.service.SuggestionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SuggestionController {

    @Resource
    private SuggestionService suggestionService;

    @RequestMapping(value = "/suggestion/skill/{query}", method = RequestMethod.GET)
    public List<String> suggestSkill(@PathVariable String query) {
        return suggestionService.suggestSkills(query);
    }

    @RequestMapping(value = "/suggestion/jobTitle/{query}", method = RequestMethod.GET)
    public SuggestionJobTitleModel suggestJobTitle(@PathVariable String query) {
        SuggestionJobTitleModel model = new SuggestionJobTitleModel();
        List<SuggestionJobTitleItem> items = new ArrayList<>();
        for (String jobTitle : suggestionService.suggestJobTitles(query)) {
            items.add(new SuggestionJobTitleItem(jobTitle));
        }
        model.setItems(items);
        return model;
    }
}
