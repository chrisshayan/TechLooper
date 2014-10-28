package com.techlooper.service;

import com.techlooper.model.VNWConfigurationResponse;
import com.techlooper.model.VNWJobSearchRequest;
import com.techlooper.model.VNWJobSearchResponse;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
public interface JobSearchService {

    /**
     * Loads the configuration of the VietnamWorks search
     *
     * @return See more at {@link com.techlooper.model.VNWConfigurationResponse}
     */
    VNWConfigurationResponse getConfiguration();

    /**
     * This method executes the search VietnamWorks search API
     *
     * @param jobSearchRequest See more at {@link com.techlooper.model.VNWJobSearchRequest}
     * @return Response of the search in format of {@link com.techlooper.model.VNWJobSearchResponse}
     */
    VNWJobSearchResponse searchJob(VNWJobSearchRequest jobSearchRequest);

}
