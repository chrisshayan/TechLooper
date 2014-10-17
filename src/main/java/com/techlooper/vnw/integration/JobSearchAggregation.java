package com.techlooper.vnw.integration;

import com.techlooper.enu.RouterConstant;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

/**
 * Created by phuonghqh on 10/17/14.
 */
@Component("vnwJobSearchAggregation")
public class JobSearchAggregation implements AggregationStrategy {

  public Exchange aggregate(Exchange original, Exchange resource) {//resource.getIn().getBody()
    original.setProperty(RouterConstant.VNW_CONFIG, resource.getIn().getBody());
    return original;//original.getIn().getBody()//JobSearchRequest
  }
}
