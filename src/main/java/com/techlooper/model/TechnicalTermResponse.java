package com.techlooper.model;

public class TechnicalTermResponse {

   private String name;
   
   private TechnicalTermEnum term;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public TechnicalTermEnum getTerm() {
      return term;
   }

   public void setTerm(TechnicalTermEnum term) {
      this.term = term;
   }


   public static class Builder {
      
      private TechnicalTermResponse instance = new TechnicalTermResponse();
      
      public Builder withName(String name) {
         instance.setName(name);
         return this;
      }
      
      public Builder withTerm(TechnicalTermEnum term) {
         instance.setTerm(term);
         return this;
      }
      
      public TechnicalTermResponse build() {
         return instance;
      }
   }

}
