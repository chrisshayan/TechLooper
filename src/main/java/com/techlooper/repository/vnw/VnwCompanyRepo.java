package com.techlooper.repository.vnw;

import com.techlooper.entity.vnw.VnwCompany;

public interface VnwCompanyRepo extends ReadOnlyRepository<VnwCompany, Long> {

    VnwCompany findByCompanyId(Long companyId);

}
