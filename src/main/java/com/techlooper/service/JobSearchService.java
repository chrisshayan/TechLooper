package com.techlooper.service;

import com.techlooper.model.VNWConfigurationResponse;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VNWJobSearchResponse;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
public interface JobSearchService {

    VNWConfigurationResponse getConfiguration();

    VNWJobSearchResponse searchJob(VNWJobSearchRequest jobSearchRequest);

}
