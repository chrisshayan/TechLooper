package com.techlooper.service;

import com.techlooper.model.*;

import java.util.Map;

/**
 * Created by chrisshayan on 7/14/14.
 */
public interface JobStatisticService {

    /**
     * Counting jobs by term
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTerm
     */
    public Long count(final TechnicalTerm term);

    /**
     * Counting jobs by each skill within a certain period in the past
     *
     * @param term           See more at {@link com.techlooper.model.TechnicalTerm}
     * @param histogramEnums See more at {@link com.techlooper.model.HistogramEnum}
     * @return the skill statistic {@link com.techlooper.model.SkillStatisticResponse}
     */
    SkillStatisticResponse countJobsBySkill(TechnicalTerm term, HistogramEnum... histogramEnums);

    public Long countJobsBySkillWithinPeriod(final String skill, final HistogramEnum period);

    public Long countTotalITJobsWithinPeriod(HistogramEnum period);

    public TermStatisticResponse generateTermStatistic(TermStatisticRequest term, HistogramEnum histogramEnum);

    public Map<String, Double> getAverageSalaryBySkill(TechnicalTerm term, Integer jobLevelId);
}
