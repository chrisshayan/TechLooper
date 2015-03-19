package com.techlooper.repository;

import com.techlooper.model.SocialConfig;
import com.techlooper.model.TechnicalTerm;
import com.techlooper.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by NguyenDangKhoa on 11/25/14.
 */
@Repository
public class JsonConfigRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonConfigRepository.class);

    @Value("classpath:skill.json")
    private Resource skillJsonResource;

    @Value("classpath:socialConfig.json")
    private Resource socialConfigResource;

    @Value("classpath:githubTalentSearchScore.json")
    private Resource githubTalentSearchScore;


    /**
     * Load technical term and skill from JSON file and put them into the cache
     *
     * @return list of terms {@link com.techlooper.model.SocialConfig}
     */
    @Cacheable(value = "SOCIAL_CONFIG")
    public List<SocialConfig> getSocialConfig() {
        return JsonUtils.toList(socialConfigResource, SocialConfig.class).get();
    }

    @Cacheable(value = "SKILL_CONFIG")
    public List<TechnicalTerm> getSkillConfig() {
        return JsonUtils.toList(skillJsonResource, TechnicalTerm.class).get();
    }

    @Cacheable(value = "GITHUB_TALENT_SCORE_CONFIG")
    public Map<String,Integer> getGithubTalentScore() {
        return JsonUtils.toMap(githubTalentSearchScore);
    }

    /**
     * Intentionally, this method is just used to invalidate the cache and trigger reloading new term and skill from JSON file
     */
    @CacheEvict(value = {"SOCIAL_CONFIG", "SKILL_CONFIG", "GITHUB_TALENT_SCORE_CONFIG"}, allEntries = true)
    public void refresh() {
    }

    /**
     * This method find the matching technical term
     *
     * @param termKey the term to look for
     * @return Correct instance of {@link com.techlooper.model.TechnicalTerm} related to termKey
     */
    public TechnicalTerm findByKey(String termKey) {
        return getSkillConfig().stream().filter(term -> term.getKey().equals(termKey)).findFirst().get();
    }

    public Resource getSkillJsonResource() {
        return skillJsonResource;
    }
}
