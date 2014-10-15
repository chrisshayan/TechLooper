package com.techlooper.config;

import org.apache.camel.CamelContext;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

        // TODO: use Aggregator to support get result from multiple source, such as: vietnamworks, github...
        from("direct:jobs/search").
          process(vnwJobSearchProcessor).
          to("https4://api.vietnamworks.com/jobs/search").unmarshal(vnwJobSearchDataFormat).
          to("direct:jobs/search/vnw");

//        from("direct:jobs/search")
//          .to("log:output?showAll=true");
      }
    };
  }
}
