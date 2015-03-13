package com.techlooper.repository.talentsearch;

import com.techlooper.config.ConfigurationTest;
import com.techlooper.config.ElasticsearchUserImportConfiguration;
import com.techlooper.entity.userimport.UserImportEntity;
import com.techlooper.model.TalentSearchRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ConfigurationTest.class, ElasticsearchUserImportConfiguration.class})
public class VietnamworksTalentSearchRepositoryITCase {

    @Resource
    private VietnamworksTalentSearchRepository vietnamworksTalentSearchRepository;

    @Test
    public void testFindTalent() throws Exception {
        TalentSearchRequest.Builder searchParam = new TalentSearchRequest.Builder();
        searchParam.withSkills("Java").withPageSize(20).withPageIndex(0);
        List<UserImportEntity> userEntities = vietnamworksTalentSearchRepository.findTalent(searchParam.build());
        assertTrue(userEntities.size() > 0);
    }
}