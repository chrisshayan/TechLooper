package com.techlooper.config;

import javax.annotation.Resource;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = "com.techlooper")
@EnableElasticsearchRepositories(basePackages = "com.techlooper.repository")
@PropertySource("classpath:techlooper.properties")
@EnableScheduling
public class CoreConfiguration {

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
   public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
      return new PropertySourcesPlaceholderConfigurer();
   }
}
