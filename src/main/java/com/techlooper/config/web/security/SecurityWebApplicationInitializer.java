package com.techlooper.config.web.security;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 * Created by NguyenDangKhoa on 11/27/14.
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    protected String getDispatcherWebApplicationContextSuffix() {
        return AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
    }
}