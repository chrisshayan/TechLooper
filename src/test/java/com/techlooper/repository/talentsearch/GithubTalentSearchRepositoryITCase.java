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
public class GithubTalentSearchRepositoryITCase {

    @Resource
    private GithubTalentSearchRepository githubTalentSearchRepository;

    @Test
    public void testFindTalent() throws Exception {
        TalentSearchRequest.Builder searchParam = new TalentSearchRequest.Builder();
        searchParam.withSkills("Java").withLocations("Vietnam")
                .withSortByField("profiles.GITHUB.numberOfRepositories").withCompanies("Navigos")
                .withPageSize(20).withPageIndex(0);
        List<UserImportEntity> userEntities = githubTalentSearchRepository.findTalent(searchParam.build());
        assertTrue(userEntities.size() > 0);
    }

    @Test
    public void testCountTalent() throws Exception {
        TalentSearchRequest.Builder searchParam = new TalentSearchRequest.Builder();
        searchParam.withSkills("Java");
        long count = githubTalentSearchRepository.countTalent(searchParam.build());
        assertTrue(count > 0);
    }
}