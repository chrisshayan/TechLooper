package com.techlooper.model;

public class JobStatisticResquest {

   private String term;

   public String getTerm() {
      return term;
   }

   public void setTerm(String term) {
      this.term = term;
   }
   
   public static class Builder {
      
      private JobStatisticResquest instance = new JobStatisticResquest();
      
      public Builder withTerm(String term) {
         instance.setTerm(term);;
         return this;
      }
      
      public JobStatisticResquest build() {
         return instance;
      }
   }

}
