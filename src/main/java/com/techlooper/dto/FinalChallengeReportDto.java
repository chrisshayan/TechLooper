package com.techlooper.dto;

import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengeDto;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeRegistrantPhaseItem;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by phuonghqh on 12/1/15.
 */
public class FinalChallengeReportDto extends ChallengeDto {

  private Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> registrantPhaseAgg;

  public Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> getRegistrantPhaseAgg() {
    return registrantPhaseAgg;
  }

  public void setRegistrantPhaseAgg(Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> registrantPhaseAgg) {
    this.registrantPhaseAgg = registrantPhaseAgg;
  }
}
