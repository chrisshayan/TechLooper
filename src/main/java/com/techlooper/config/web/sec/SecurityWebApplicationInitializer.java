package com.techlooper.config.web.sec;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

/**
 * Created by phuonghqh on 6/25/15.
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

  protected String getDispatcherWebApplicationContextSuffix() {
    return AbstractDispatcherServletInitializer.DEFAULT_SERVLET_NAME;
  }
}