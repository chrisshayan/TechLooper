package com.techlooper.config;

import com.techlooper.model.TechnicalSkillEnumMap;
import com.techlooper.model.TechnicalTermEnum;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.JobSearchService;
import com.techlooper.service.impl.JobQueryBuilderImpl;
import com.techlooper.service.impl.VietnamWorksJobSearchService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;


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
    public JobQueryBuilder jobQueryBuilder() {
        return new JobQueryBuilderImpl();
    }

    // TODO : separate to another config
    @Bean
    public TechnicalSkillEnumMap technicalSkillEnumMap() {
        TechnicalSkillEnumMap technicalSkillEnumMap = new TechnicalSkillEnumMap();
        Stream.of(TechnicalTermEnum.values()).forEach(term -> {
//            if (TechnicalTermEnum.EMPTY != term) {
            String termKey = environment.getProperty(term.value().replaceAll(" ", "_"));
            Optional<String> skillOptional = Optional.ofNullable(termKey);
            if (skillOptional.isPresent()) {
                String[] skills = StringUtils.split(skillOptional.get(), ',');
                technicalSkillEnumMap.put(term, Arrays.asList(skills));
            }
//            }
        });
        return technicalSkillEnumMap;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
