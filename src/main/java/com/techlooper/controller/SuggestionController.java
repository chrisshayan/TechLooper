package com.techlooper.controller;

import com.techlooper.model.SkillSuggestionItem;
import com.techlooper.model.SkillSuggestionModel;
import com.techlooper.service.SuggestionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@RestController
public class SuggestionController {

    @Resource
    private SuggestionService suggestionService;

    @RequestMapping(value = "/suggestion/skill/{query}", method = RequestMethod.POST)
    public SkillSuggestionModel suggestSkill(@PathVariable String query) {
        SkillSuggestionItem skillSuggestionItem = new SkillSuggestionItem();
        skillSuggestionItem.setText("Java Developer");
        SkillSuggestionModel skillSuggestionModel = new SkillSuggestionModel();
        skillSuggestionModel.setSkills(Arrays.asList(skillSuggestionItem));
        return skillSuggestionModel;
//        return suggestionService.suggestSkills(query);
    }

    @RequestMapping(value = "/suggestion/jobTitle/{query}", method = RequestMethod.POST)
    public List<String> suggestJobTitle(@PathVariable String query) {
        return Arrays.asList("Java Developer", "PHP Developer");
//        return suggestionService.suggestJobTitles(query);
    }
}
