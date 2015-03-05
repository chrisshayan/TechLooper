package com.techlooper.repository;

import com.techlooper.model.TechnicalTerm;
import com.techlooper.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by NguyenDangKhoa on 11/25/14.
 */
@Repository
public class TechnicalTermRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TechnicalTermRepository.class);

    @Value("classpath:skill.json")
    private Resource skillJsonResource;

    /**
     * Load technical term and skill from JSON file and put them into the cache
     *
     * @return list of terms {@link com.techlooper.model.TechnicalTerm}
     */
    @Cacheable(value = "TECHNICAL_TERM_CACHE")
    public List<TechnicalTerm> findAll() {
        Optional<List<TechnicalTerm>> termOptional = Optional.empty();
        try {
            String jsonSkill = IOUtils.toString(skillJsonResource.getInputStream(), "UTF-8");
            termOptional = JsonUtils.toList(jsonSkill, TechnicalTerm.class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return termOptional.get();
    }

    /**
     * Intentionally, this method is just used to invalidate the cache and trigger reloading new term and skill from JSON file
     */
    @CacheEvict(value = "TECHNICAL_TERM_CACHE", allEntries = true)
    public void refresh() {

    }

    /**
     * This method find the matching technical term
     *
     * @param termKey the term to look for
     * @return Correct instance of {@link com.techlooper.model.TechnicalTerm} related to termKey
     */
    public TechnicalTerm findByKey(String termKey) {
        return findAll().stream().filter(term -> term.getKey().equals(termKey)).findFirst().get();
    }

    public Resource getSkillJsonResource() {
        return skillJsonResource;
    }
}
