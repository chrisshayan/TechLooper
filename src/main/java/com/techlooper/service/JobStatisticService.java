package com.techlooper.service;

import com.techlooper.model.TechnicalTermEnum;

/**
 * Created by chrisshayan on 7/14/14.
 */
public interface JobStatisticService {
   /**
    * Counting BA jobs
    * @return number of jobs
    * @see com.techlooper.model.TechnicalTermEnum
    */
   Long countBAJobs();
   
   /**
    * Counting Project Manager jobs
    * @return number of jobs
    * @see com.techlooper.model.TechnicalTermEnum
    */
   Long countProjectManagerJobs();
   
   
    /**
     * Counting PHP jobs
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countPhpJobs();

    /**
     * Counting Java jobs
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countJavaJobs();

    /**
     * Counting .NET jobs
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countDotNetJobs();

    /**
     * Counts the matching jobs to relevant {@code TechnicalTermEnum}
     * @param technicalTermEnum a {@code TechnicalTermEnum} to determine which technology search must happen.
     * @return a {@code Long} that represents number of matching jobs.
     */
    Long count(final TechnicalTermEnum technicalTermEnum);

   public abstract Long countRubyJobs();

   public abstract Long countPythonJobs();

   public abstract Long countDBAJobs();

   public abstract Long countQAJobs();

}
