package com.techlooper.service;

import com.techlooper.entity.vnw.VnwCompany;
import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.model.LeadEventEnum;

public interface LeadAPIService {

    int createNewLead(VnwUser employer, VnwCompany company, LeadEventEnum leadEvent);

}
