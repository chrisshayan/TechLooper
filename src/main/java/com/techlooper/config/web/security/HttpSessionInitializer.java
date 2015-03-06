package com.techlooper.config.web.security;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 * Created by phuonghqh on 1/2/15.
 */
public class HttpSessionInitializer extends AbstractHttpSessionApplicationInitializer {

    protected String getDispatcherWebApplicationContextSuffix() {
        return AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
    }
}
