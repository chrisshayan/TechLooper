package com.techlooper.controller;

import com.techlooper.service.SuggestionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    public List<String> suggestJobTitle(@PathVariable String query) {
        return suggestionService.suggestJobTitles(query);
    }
}
