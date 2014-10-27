package com.techlooper.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.CssLinkResourceTransformer;
import org.springframework.web.servlet.resource.GzipResourceResolver;

import javax.annotation.Resource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.techlooper.controller"})
public class WebConfig extends WebMvcConfigurerAdapter {

  @Resource
  private Environment environment;

  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }

  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**").addResourceLocations(environment.getProperty("webapp.resource.location"))
      .resourceChain(true)
            /*
             * .addResolver(new CachingResourceResolver(cacheManager,
             * "default"))
             */.addResolver(new GzipResourceResolver())
      // .addTransformer(new CachingResourceTransformer(cacheManager,
      // "default"))
      .addTransformer(new CssLinkResourceTransformer());
    // registry.addResourceHandler("/index.html").addResourceLocations("classpath:/static/index.html");
  }
}
