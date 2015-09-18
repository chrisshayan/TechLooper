package com.techlooper.repository.vnw;

import com.techlooper.entity.vnw.VnwCompany;

public interface VnwCompanyRepo extends ReadOnlyRepository<VnwCompany, Long> {

    @Override
    VnwCompany findOne(Long companyId);

}
