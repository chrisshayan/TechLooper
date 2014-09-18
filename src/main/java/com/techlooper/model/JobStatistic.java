package com.techlooper.model;

public class JobStatistic {

   private Long count;

   private TechnicalTermEnum term;

   public Long getCount() {
      return count;
   }

   public void setCount(Long count) {
      this.count = count;
   }

   public TechnicalTermEnum getTerm() {
      return term;
   }

   public void setTerm(TechnicalTermEnum term) {
      this.term = term;
   }

   public static Builder createBuilder() {
      return new Builder();
   }

   public static class Builder {
      private JobStatistic jobStatistic;

      public Builder() {
         jobStatistic = new JobStatistic();
      }

      public Builder withTerm(TechnicalTermEnum term) {
         jobStatistic.setTerm(term);
         return this;
      }

      public Builder withCount(Long count) {
         jobStatistic.setCount(count);
         return this;
      }

      public JobStatistic build() {
         return jobStatistic;
      }
   }

}
