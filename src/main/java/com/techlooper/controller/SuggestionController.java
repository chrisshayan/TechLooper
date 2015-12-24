package com.techlooper.controller;

import com.techlooper.model.SuggestionItem;
import com.techlooper.model.SuggestionResponse;
import com.techlooper.service.SuggestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trim;

@RestController
public class SuggestionController {

    @Resource
    private SuggestionService suggestionService;

//    @CrossOrigin
    @RequestMapping(value = "/suggestion/skill/{query}", method = RequestMethod.GET)
    public SuggestionResponse suggestSkill(@PathVariable String query) {
        SuggestionResponse response = new SuggestionResponse();
        List<SuggestionItem> items = suggestionService.suggestSkills(trim(query)).stream()
                .map(SuggestionItem::new).collect(Collectors.toList());
        response.setItems(items);
        return response;
    }

    @CrossOrigin
    @RequestMapping(value = "/suggestion/skills/{query}", method = RequestMethod.GET)
    public List<String> suggestSkills(@PathVariable String query) {
        return suggestionService.suggestSkills(trim(query));
    }

    @RequestMapping(value = "/suggestion/jobTitle/{query}", method = RequestMethod.GET)
    public SuggestionResponse suggestJobTitle(@PathVariable String query) {
        SuggestionResponse response = new SuggestionResponse();
        List<SuggestionItem> items = suggestionService.suggestJobTitles(trim(query)).stream()
                .map(SuggestionItem::new).collect(Collectors.toList());
        response.setItems(items);
        return response;
    }

    @CrossOrigin
    @RequestMapping(value = "/widget/suggestion/jobTitle/{query}", method = RequestMethod.GET)
    public List<String> suggestWidgetJobTitle(@PathVariable String query) {
        return suggestionService.suggestJobTitles(trim(query));
    }
}
