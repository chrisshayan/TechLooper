package com.techlooper.service;

import com.techlooper.model.PeriodEnum;
import com.techlooper.model.SkillStatisticResponse;
import com.techlooper.model.TechnicalTermEnum;

import java.time.LocalDate;

/**
 * Created by chrisshayan on 7/14/14.
 */
public interface JobStatisticService {
    /**
     * Counting BA jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countBAJobs();

    /**
     * Counting Project Manager jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countProjectManagerJobs();


    /**
     * Counting PHP jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countPhpJobs();

    /**
     * Counting Java jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countJavaJobs();

    /**
     * Counting .NET jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countDotNetJobs();

    /**
     * Counts the matching jobs to relevant {@code TechnicalTermEnum}
     *
     * @param technicalTermEnum a {@code TechnicalTermEnum} to determine which technology search must happen.
     * @return a {@code Long} that represents number of matching jobs.
     */
    Long count(final TechnicalTermEnum technicalTermEnum);

    /**
     * Counting Ruby jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countRubyJobs();

    /**
     * Counting Python jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countPythonJobs();

    /**
     * Counting DBA jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countDBAJobs();

    /**
     * Counting QA jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countQAJobs();

    /**
     * Counting all technical jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countTechnicalJobs();

    /**
     * Counting number of jobs by technical term and its skill
     *
     * @param technicalTermEnum
     * @param skill
     * @param untilApprovedDate
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTermEnum
     */
    Long countTechnicalJobsBySkill(TechnicalTermEnum technicalTermEnum, String skill, LocalDate untilApprovedDate);

    SkillStatisticResponse countJobsBySkill(TechnicalTermEnum term, PeriodEnum period);
}
