package com.techlooper.service;

import com.techlooper.model.HistogramEnum;
import com.techlooper.model.SkillStatisticResponse;
import com.techlooper.model.TechnicalTerm;

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
     * Counting all technical jobs
     *
     * @return number of jobs
     * @see com.techlooper.model.TechnicalTerm
     */
    Long countTechnicalJobs();

    SkillStatisticResponse countJobsBySkill(TechnicalTerm term, HistogramEnum... histogramEnums);
}
