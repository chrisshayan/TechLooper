package com.techlooper.controller;

import com.techlooper.service.SkillSuggestionService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    private SkillSuggestionService skillSuggestionService;

    @RequestMapping(value = "/suggestion/skill/{query}", method = RequestMethod.POST)
    public List<String> suggestSkill(@PathVariable String query) {
        return skillSuggestionService.suggestSkills(query);
    }

    @RequestMapping(value = "/suggestion/jobTitle/{query}", method = RequestMethod.POST)
    public List<String> suggestJobTitle(@PathVariable String query) {
        return Arrays.asList("Java Developer", "PHP Developer");
    }
}
