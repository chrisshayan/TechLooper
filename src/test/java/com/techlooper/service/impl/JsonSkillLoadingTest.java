package com.techlooper.service.impl;

import com.techlooper.model.TechnicalTerm;
import com.techlooper.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class JsonSkillLoadingTest {

    private List<TechnicalTerm> terms;

    @Before
    public void setUp() throws IOException {
        String jsonSkill = IOUtils.toString(getClass().getResourceAsStream("/expect/skill.json"), "UTF-8");
        Optional<List<TechnicalTerm>> termsOptional = JsonUtils.toList(jsonSkill, TechnicalTerm.class);
        terms = termsOptional.get();
        assertNotNull(terms);
    }

    @Test
    public void testRequiredFieldNotEmpty() {
        // Term key, search texts and skills are mandatory fields.
        terms.stream().forEach(term -> {
            assertNotNull(term.getKey());
            assertNotEquals("Term key shouldn't be empty.", "", term.getKey());
            assertNotNull(term.getSearchTexts());
            assertNotEquals("Each search texts have to contain at least one keyword for search.", 0, term.getSearchTexts().size());
            assertNotNull(term.getSkills());
            assertNotEquals("Each term has to contain at least one skill.", 0, term.getSkills().size());
        });
    }
}
