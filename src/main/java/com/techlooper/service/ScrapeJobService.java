package com.techlooper.service;

import com.techlooper.model.KimonoJobModel;
import com.techlooper.model.VNWJobSearchResponseDataItem;

import java.util.Set;

public interface ScrapeJobService {

    void save(KimonoJobModel jobModel);

    void save(Set<VNWJobSearchResponseDataItem> vnwJobs, Boolean isTopPriority);

}
