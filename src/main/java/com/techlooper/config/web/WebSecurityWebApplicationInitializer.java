package com.techlooper.config.web;

import com.techlooper.config.CoreConfiguration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by NguyenDangKhoa on 11/27/14.
 */
public class WebSecurityWebApplicationInitializer
        extends AbstractSecurityWebApplicationInitializer {

    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{CoreConfiguration.class};
    }
}