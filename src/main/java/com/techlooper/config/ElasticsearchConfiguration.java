package com.techlooper.config;

import com.techlooper.model.TechnicalSkillEnumMap;
import com.techlooper.model.TechnicalTermEnum;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by phuonghqh on 10/13/14.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.techlooper.repository")
public class ElasticsearchConfiguration {

    @Resource
    private Environment environment;

    @Resource
    private TransportClient transportClient;

    @Bean
    public FactoryBean<TransportClient> transportClient() throws Exception {
        TransportClientFactoryBean factory = new TransportClientFactoryBean();
        factory.setClusterName(environment.getProperty("elasticsearch.cluster.name"));
        factory.setClusterNodes(environment.getProperty("elasticsearch.host"));
        return factory;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(transportClient);
    }

    @Bean
    public TechnicalSkillEnumMap technicalSkillEnumMap() {
        TechnicalSkillEnumMap technicalSkillEnumMap = new TechnicalSkillEnumMap();
        Stream.of(TechnicalTermEnum.values()).forEach(term -> {
            final String termKey = environment.getProperty(term.name());
            Optional<String> skillOptional = Optional.ofNullable(termKey);
            if (skillOptional.isPresent()) {
                String[] skills = StringUtils.split(skillOptional.get(), ',');
                technicalSkillEnumMap.put(term, Arrays.asList(skills));
            }
        });
        return technicalSkillEnumMap;
    }
}
