package com.techlooper.service;

import com.techlooper.model.vnw.VNWConfigurationResponse;
import com.techlooper.model.vnw.VNWJobSearchRequest;
import com.techlooper.model.vnw.VNWJobSearchResponse;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
public interface JobSearchService {

    VNWConfigurationResponse getConfiguration();

    VNWJobSearchResponse searchJob(VNWJobSearchRequest jobSearchRequest);

}
