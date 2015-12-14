package com.techlooper.service.impl;

import com.techlooper.config.ElasticsearchConfiguration;
import com.techlooper.config.SuggestionServiceConfigurationTest;
import com.techlooper.service.SuggestionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ElasticsearchConfiguration.class, SuggestionServiceConfigurationTest.class})
public class SuggestionServiceImplTest {

    @Resource
    private SuggestionService suggestionService;

    @Test
    public void testSuggestSkills() throws Exception {
        String query = "Java";
        List<String> skills = suggestionService.suggestSkills(query);
        skills.forEach(skill -> assertTrue(skill.toLowerCase().contains(query.toLowerCase())));
        assertTrue(skills.size() > 0);
    }

    @Test
    public void testSuggestSkillsNoSkill() throws Exception {
        String query = "Senior Java Developer";
        List<String> jobTitles = suggestionService.suggestJobTitles(query);
        assertTrue(jobTitles.isEmpty());
    }

    @Test
    public void testTheBestMatchJobTitle() throws Exception {
        String query = "Senior Java Developer ( High Salary And Good Working Environment)";
        List<String> jobTitles = suggestionService.searchJobTitles(query);
        assertTrue(!jobTitles.isEmpty());
    }
}