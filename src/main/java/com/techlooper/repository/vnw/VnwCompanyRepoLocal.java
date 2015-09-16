package com.techlooper.repository.vnw;

import com.techlooper.entity.vnw.VnwCompany;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class VnwCompanyRepoLocal implements VnwCompanyRepo {

    @Override
    public VnwCompany findByCompanyId(Long companyId) {
        return null;
    }

    @Override
    public VnwCompany findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<VnwCompany> findAll() {
        return null;
    }
}
