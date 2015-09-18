package com.techlooper.config.web;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.CachingResourceResolver;
import org.springframework.web.servlet.resource.CachingResourceTransformer;
import org.springframework.web.servlet.resource.CssLinkResourceTransformer;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.Resource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.techlooper.controller"})
public class WebConfiguration extends WebMvcConfigurerAdapter {

  @Resource
  private Environment environment;

  @Resource
  private ApplicationContext applicationContext;

  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("index.html");
  }

  @Bean
  public ViewResolver viewResolver() {
    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setSuffix("");
    return viewResolver;
  }

  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/talentsearch/**").addResourceLocations("/rs/");

    registry.addResourceHandler("/**")
      .addResourceLocations(environment.getProperty("webapp.resource.location"))
      .resourceChain(true).addResolver(new GzipResourceResolver());

//    CacheManager cacheManager = applicationContext.getBean(CacheManager.class);
//    if (ArrayUtils.contains(environment.getActiveProfiles(), "prod")) {
//      resourceChainRegistration.addResolver(new CachingResourceResolver(cacheManager, "default"))
//        .addTransformer(new CachingResourceTransformer(cacheManager, "default"))
//        .addTransformer(new CssLinkResourceTransformer());
//    }
  }
}
