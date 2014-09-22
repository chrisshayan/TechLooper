package com.techlooper.config;

import javax.annotation.Resource;

import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.*;
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
//The envTarget variable can be set in the OS/environment or as a parameter to the JVM command line: -DenvTarget=dev
@PropertySource("classpath:${envTarget:techlooper}.properties")
@EnableScheduling
@EnableAspectJAutoProxy
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
