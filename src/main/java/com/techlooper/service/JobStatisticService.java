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
     * Counting jobs by each skill within a certain period in the past
     *
     * @return the skill statistic {@link com.techlooper.model.SkillStatisticResponse}
     * @param term See more at {@link com.techlooper.model.TechnicalTerm}
     * @param histogramEnums See more at {@link com.techlooper.model.HistogramEnum}
     */
    SkillStatisticResponse countJobsBySkill(TechnicalTerm term, HistogramEnum... histogramEnums);
}
