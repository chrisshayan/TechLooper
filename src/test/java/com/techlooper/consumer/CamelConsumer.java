package com.techlooper.consumer;

import com.techlooper.model.JobSearchResponse;
import org.apache.camel.Consume;
import org.apache.camel.RecipientList;
import org.junit.Assert;
import org.springframework.stereotype.Component;

/**
 * Created by phuonghqh on 10/20/14.
 */
@Component
public class CamelConsumer {

  @Consume(uri = "direct:jobs/search/response")
  @RecipientList
  public void onJobSearch(JobSearchResponse jobSearchResponse) {
    Assert.assertNotNull(jobSearchResponse);
  }

}
