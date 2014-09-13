package com.techlooper.config;

import javax.annotation.Resource;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.techlooper")
@EnableWebMvc
@EnableElasticsearchRepositories(basePackages = "com.techlooper.repository")
@PropertySource("classpath:techlooper.properties")
public class TechLooperConfiguration {

   @Resource
   private Environment environment;

   @Value("${elasticsearch.host}")
   private String elasticsearchHost;

   @Value("${elasticsearch.cluster.name}")
   private String elasticsearchClusterName;

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
}
