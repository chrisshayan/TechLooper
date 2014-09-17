package com.techlooper.model;

public class JobStatisticResponse {

   private Long count;

   public Long getCount() {
      return count;
   }

   public void setCount(Long count) {
      this.count = count;
   }

   public static Builder createBuilder() {
      return new Builder();
   }

   public static class Builder {
      private JobStatisticResponse jobStatistic;

      public Builder() {
         jobStatistic = new JobStatisticResponse();
      }

      public Builder withCount(Long count) {
         jobStatistic.setCount(count);
         return this;
      }

      public JobStatisticResponse build() {
         return jobStatistic;
      }
   }

}
