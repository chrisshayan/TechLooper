package com.techlooper.config;

import javax.annotation.Resource;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.CssLinkResourceTransformer;
import org.springframework.web.servlet.resource.GzipResourceResolver;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

   @Resource
   private CacheManager cacheManager;

   public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
      configurer.enable();
   }

   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/**").addResourceLocations("/assets/**").resourceChain(true)
      /* .addResolver(new CachingResourceResolver(cacheManager, "default")) */.addResolver(new GzipResourceResolver())
      // .addTransformer(new CachingResourceTransformer(cacheManager,
      // "default"))
            .addTransformer(new CssLinkResourceTransformer());
      // registry.addResourceHandler("/index.html").addResourceLocations("classpath:/static/index.html");
   }
}
