package com.techlooper.vnw.integration;

import com.techlooper.model.JobSearchRequest;
import net.minidev.json.JSONObject;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

/**
 * Created by phuonghqh on 10/16/14.
 */

@Component("vnwJobSearchAggregationStrategy")
public class JobSearchAggregationStrategy implements AggregationStrategy {

  public Exchange aggregate(Exchange original, Exchange resource) {
    JobSearchRequest jobSearchRequest = original.getIn().getBody(JobSearchRequest.class);
    JSONObject configuration = resource.getIn().getBody(JSONObject.class);
    JobSearchModel.Builder model = new JobSearchModel.Builder().withConfiguration(configuration).withRequest(jobSearchRequest);
    if (original.getPattern().isOutCapable()) {
      original.getOut().setBody(model.build(), JobSearchModel.class);
    } else {
      original.getIn().setBody(model.build(), JobSearchModel.class);
    }
    return original;
  }
}
