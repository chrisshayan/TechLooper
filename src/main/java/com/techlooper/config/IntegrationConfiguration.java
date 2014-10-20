package com.techlooper.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by phuonghqh on 10/13/14.
 */
@Configuration
@ComponentScan({"com.techlooper.integration", "com.techlooper.vnw.integration"})
public class IntegrationConfiguration extends CamelConfiguration {

  @Resource
  private RouteBuilder jobSearchRouter;

  public List<RouteBuilder> routes() {
    return Arrays.asList(jobSearchRouter);
  }
}
