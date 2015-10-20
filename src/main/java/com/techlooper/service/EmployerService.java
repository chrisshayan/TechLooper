package com.techlooper.service;

import com.techlooper.dto.DashBoardInfo;
import com.techlooper.dto.EmailSettingDto;
import com.techlooper.entity.vnw.VnwCompany;
import com.techlooper.entity.vnw.VnwUser;

/**
 * Created by phuonghqh on 8/11/15.
 */
public interface EmployerService {

    DashBoardInfo getDashboardInfo(String username);

    VnwCompany findCompanyById(Long companyId);

    VnwUser findEmployerByUsername(String employerUsername);

    EmailSettingDto saveEmployerEmailSetting(EmailSettingDto emailSettingDto);

    EmailSettingDto findEmployerEmailSetting(String employerEmail);
}
