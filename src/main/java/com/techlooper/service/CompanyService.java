package com.techlooper.service;

import com.techlooper.entity.CompanyEntity;

/**
 * Created by phuonghqh on 4/2/15.
 */
public interface CompanyService {

  CompanyEntity findById(Long id);

  CompanyEntity findByName(String companyName);
}
