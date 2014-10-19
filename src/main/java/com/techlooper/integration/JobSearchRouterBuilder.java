package com.techlooper.integration;

import com.techlooper.enu.RouterConstant;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cache.CacheConstants;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.spi.DataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by phuonghqh on 10/19/14.
 */

@Component
public class JobSearchRouterBuilder extends RouteBuilder {

  @Resource(name = "vnwJobSearchProcessor")
  private Processor vnwJobSearchProcessor;

  @Resource(name = "vnwJobSearchDataFormat")
  private DataFormat vnwJobSearchDataFormat;

  @Resource(name = "vnwConfigurationProcessor")
  private Processor vnwConfigurationProcessor;

  @Resource(name = "vnwConfigurationDataFormat")
  private DataFormat vnwConfigurationDataFormat;

  @Resource(name = "vnwJobSearchAggregation")
  private AggregationStrategy vnwJobSearchAggregation;

  @Value("${vnw.api.job.search.url}")
  private String searchUrl;

  @Value("${vnw.api.configuration.url}")
  private String configurationUrl;

  public void configure() throws Exception {
    from("cache://vnwConfiguration" +
      "?maxElementsInMemory=1000" +
      "&memoryStoreEvictionPolicy=" +
      "MemoryStoreEvictionPolicy.LFU" +
      "&overflowToDisk=true" +
      "&eternal=true" +
      "&timeToLiveSeconds=86400" +
      "&timeToIdleSeconds=3600" +
      "&diskPersistent=true" +
      "&diskExpiryThreadIntervalSeconds=86400").to("direct:donothing");

    from("direct:jobs/search/vnw/configuration")
      .setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_GET))
      .setHeader(CacheConstants.CACHE_KEY, constant(RouterConstant.VNW_CONFIG))
      .to("cache://vnwConfiguration")
      .choice()
      .when(header(CacheConstants.CACHE_ELEMENT_WAS_FOUND).isNull())
      .process(vnwConfigurationProcessor)
      .to(configurationUrl).unmarshal(vnwConfigurationDataFormat)
      .setHeader(CacheConstants.CACHE_OPERATION, constant(CacheConstants.CACHE_OPERATION_ADD))
      .setHeader(CacheConstants.CACHE_KEY, constant(RouterConstant.VNW_CONFIG))
      .to("cache://vnwConfiguration")
      .end();

    from("direct:jobs/search")
      .choice()
      .when(header(RouterConstant.TO).isEqualTo(RouterConstant.VIETNAMWORKS))
      .enrich("direct:jobs/search/vnw/configuration", vnwJobSearchAggregation)
      .process(vnwJobSearchProcessor)
      .to(searchUrl).unmarshal(vnwJobSearchDataFormat)
      .end()
      .to("direct:jobs/search/response");
  }
}
