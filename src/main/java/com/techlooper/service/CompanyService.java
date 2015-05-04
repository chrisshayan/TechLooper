package com.techlooper.service;

import com.techlooper.entity.CompanyEntity;
import com.techlooper.entity.EmployerEntity;

/**
 * Created by phuonghqh on 4/2/15.
 */
public interface CompanyService {

    CompanyEntity findById(Long id);

    CompanyEntity findByName(String companyName);

    EmployerEntity findEmployerByCompanyId(Long companyId);
}
