package com.techlooper.util;

import com.techlooper.model.TechnicalTerm;
import com.techlooper.model.VNWConfigurationResponse;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class JsonUtilsTest {

    @Test
    public void testGetObjectMapper() throws Exception {
        assertNotNull(JsonUtils.getObjectMapper());
        assertThat(JsonUtils.getObjectMapper(), sameInstance(JsonUtils.getObjectMapper()));
    }

    @Test
    public void testToPOJO() throws Exception {
        String jsonConf = IOUtils.toString(getClass().getResourceAsStream("/expect/vnw-configuration.json"), "UTF-8");

        Optional<VNWConfigurationResponse> responseOptional = JsonUtils.toPOJO(jsonConf, VNWConfigurationResponse.class);
        VNWConfigurationResponse configurationResponse = responseOptional.get();

        assertNotNull(configurationResponse);
        assertNotNull(configurationResponse.getData().getLocations());
        assertNotEquals(0, configurationResponse.getData().getLocations().size());
    }

    @Test
    public void testToJSON() throws Exception {
        TechnicalTerm technicalTerm = new TechnicalTerm();
        technicalTerm.setKey("JAVA");
        technicalTerm.setLabel("Java");
        technicalTerm.setSearchTexts(Arrays.asList("java", "j2ee"));

        Optional<String> jsonOptional = JsonUtils.toJSON(technicalTerm);
        String json = jsonOptional.get();

        assertNotNull(json);
        assertThat(json, containsString("j2ee"));
    }

    @Test
    public void testToList() throws Exception {
        String jsonSkill = IOUtils.toString(getClass().getResourceAsStream("/expect/skill.json"), "UTF-8");

        Optional<List<TechnicalTerm>> termsOptional = JsonUtils.toList(jsonSkill, TechnicalTerm.class);
        List<TechnicalTerm> terms = termsOptional.get();

        assertNotNull(terms);
        TechnicalTerm javaTerm = terms.get(0);
        assertEquals("JAVA", javaTerm.getKey());
        assertThat(javaTerm.getSearchTexts(), hasItems("java", "j2ee"));
        assertEquals(2, javaTerm.getUsefulLinks().size());
        assertEquals(18, javaTerm.getSkills().size());

    }
}