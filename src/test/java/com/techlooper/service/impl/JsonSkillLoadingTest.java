package com.techlooper.service.impl;

import com.techlooper.model.TechnicalTerm;
import com.techlooper.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class JsonSkillLoadingTest {

    @Test
    public void testValidJsonFormat() throws IOException {
        String jsonSkill = IOUtils.toString(getClass().getResourceAsStream("/expect/skill.json"), "UTF-8");
        Optional<List<TechnicalTerm>> terms = JsonUtils.toList(jsonSkill, TechnicalTerm.class);
        assertNotNull(terms.get());
    }
}
