package com.techlooper.service;

import com.techlooper.model.ChallengeCriteriaDto;

/**
 * Created by phuonghqh on 10/16/15.
 */
public interface ChallengeCriteriaService {

  ChallengeCriteriaDto save(ChallengeCriteriaDto challengeCriteriaDto, String owner);
}
