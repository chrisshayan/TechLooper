package com.techlooper.config;

import com.techlooper.enu.RouterContant;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 10/13/14.
 */
@Configuration
public class IntegrationConfiguration extends SingleRouteCamelConfiguration implements InitializingBean {

  @Resource(name = "vnwJobSearchProcessor")
  private Processor vnwJobSearchProcessor;

  @Resource(name = "vnwJobSearchDataFormat")
  private DataFormat vnwJobSearchDataFormat;

  @Resource(name = "vnwConfigurationProcessor")
  private Processor vnwConfigurationProcessor;

  @Resource(name = "vnwJobSearchAggregationStrategy")
  private AggregationStrategy vnwJobSearchAggregationStrategy;

  @Resource(name = "vnwConfigurationDataFormat")
  private DataFormat vnwConfigurationDataFormat;

  @Resource
  private Environment environment;

  protected CamelContext createCamelContext() throws Exception {
    return new SpringCamelContext(getApplicationContext());
  }

  protected void setupCamelContext(CamelContext camelContext) throws Exception {
  }

  public void afterPropertiesSet() throws Exception {
    // just to make SpringDM happy do nothing here
  }

  @Bean
  public RouteBuilder route() {

    return new RouteBuilder() {
      public void configure() throws Exception {

        from("direct:jobs/search").choice()
          .when(header(RouterContant.TO).isEqualTo(RouterContant.VIETNAMWORKS))
            .enrich("direct:jobs/search/vnw/configuration", vnwJobSearchAggregationStrategy).process(vnwJobSearchProcessor)
            .to(environment.getProperty("vnw.api.job.search.url")).unmarshal(vnwJobSearchDataFormat)
          .end()
          .to("direct:jobs/search/response");

        from("direct:jobs/search/vnw/configuration")
          .process(vnwConfigurationProcessor)
          .to(environment.getProperty("vnw.api.configuration.url")).unmarshal(vnwConfigurationDataFormat);
      }
    };
  }
}
