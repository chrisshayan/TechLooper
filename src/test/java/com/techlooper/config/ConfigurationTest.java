package com.techlooper.config;

import com.techlooper.model.TechnicalTerm;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.impl.JobQueryBuilderImpl;
import com.techlooper.service.impl.VietnamWorksJobSearchService;
import com.techlooper.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


/**
 * Created by phuonghqh on 10/20/14.
 */

@Configuration
@PropertySources({
        @PropertySource("classpath:techlooper.properties"),
        @PropertySource("classpath:secret.properties"),
        @PropertySource("classpath:jobSkill.properties")})
public class ConfigurationTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource
    private Environment environment;

    @Value("classpath:skill.json")
    private org.springframework.core.io.Resource skillJsonResource;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public String vnwConfigurationJson() throws IOException {
        return IOUtils.toString(applicationContext.getResource("classpath:expect/vnw-configuration.json").getInputStream(), "UTF-8");
    }

    @Bean
    public String vnwJobSearchJson() throws IOException {
        return IOUtils.toString(applicationContext.getResource("classpath:expect/vnw-jobs.json").getInputStream(), "UTF-8");
    }

    @Bean
    public String vnwJobSearchRequestJson() throws IOException {
        return IOUtils.toString(applicationContext.getResource("classpath:expect/vnw-jobs-request.json").getInputStream(), "UTF-8");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JobSearchService jobSearchService() {
        return new VietnamWorksJobSearchService();
    }

    @Bean
    public List<TechnicalTerm> technicalTerms() throws IOException {
        String jsonSkill = IOUtils.toString(skillJsonResource.getInputStream(), "UTF-8");
        Optional<List<TechnicalTerm>> termOptional = JsonUtils.toList(jsonSkill, TechnicalTerm.class);
        return termOptional.get();
    }

    @Bean
    public JobQueryBuilder jobQueryBuilder() {
        return new JobQueryBuilderImpl();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
