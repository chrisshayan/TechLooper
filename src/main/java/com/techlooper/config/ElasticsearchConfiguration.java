package com.techlooper.config;

import com.techlooper.model.TechnicalTerm;
import com.techlooper.util.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    @Value("classpath:skill.json")
    private org.springframework.core.io.Resource skillJsonResource;

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConfiguration.class);

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

//    @Bean
//    public TechnicalSkillEnumMap technicalSkillEnumMap() {
//        TechnicalSkillEnumMap technicalSkillEnumMap = new TechnicalSkillEnumMap();
//        Stream.of(TechnicalTermEnum.values()).forEach(term -> {
//            final String termKey = environment.getProperty(term.name());
//            Optional<String> skillOptional = Optional.ofNullable(termKey);
//            if (skillOptional.isPresent()) {
//                String[] skills = StringUtils.split(skillOptional.get(), ',');
//                technicalSkillEnumMap.put(term, Arrays.asList(skills));
//            }
//        });
//        return technicalSkillEnumMap;
//    }

//    @Bean
//    public JobQueryBuilder jobQueryBuilder() {
//        try {
//            final int lastNumberOfDays = Integer.valueOf(environment.getProperty("skill.chart.lastNumberOfDays"));
//            return new ConfigurableJobQueryBuilder(lastNumberOfDays);
//        } catch (NumberFormatException ex) {
//            LOGGER.error(ex.getMessage(), ex);
//        }
//        return new MonthlyJobQueryBuilder();
//    }

    @Bean
    public List<TechnicalTerm> technicalTerms() throws IOException {
        String jsonSkill = IOUtils.toString(skillJsonResource.getInputStream(), "UTF-8");
        Optional<List<TechnicalTerm>> termOptional = JsonUtils.toList(jsonSkill, TechnicalTerm.class);
        return termOptional.get();
    }
}
