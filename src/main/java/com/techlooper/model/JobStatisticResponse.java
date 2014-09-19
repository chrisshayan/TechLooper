package com.techlooper.model;

public class JobStatisticResponse {

   private Long count;

   private TechnicalTermEnum term;

   public TechnicalTermEnum getTerm() {
      return term;
   }

   public void setTerm(TechnicalTermEnum term) {
      this.term = term;
   }

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

      private JobStatisticResponse jobStatistic = new JobStatisticResponse();

      public Builder withCount(Long count) {
         jobStatistic.setCount(count);
         return this;
      }
      
      public Builder withTerm(TechnicalTermEnum term) {
         jobStatistic.setTerm(term);
         return this;
      }

      public JobStatisticResponse build() {
         return jobStatistic;
      }
   }

}
