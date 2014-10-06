package com.techlooper.config;

import org.springframework.beans.factory.annotation.Value;
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

   @Value("${webapp.uri}")
   private String uri;

   public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
      configurer.enable();
   }

   public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/**").addResourceLocations("/" + uri + "/**").resourceChain(true)
      /* .addResolver(new CachingResourceResolver(cacheManager, "default")) */.addResolver(new GzipResourceResolver())
      // .addTransformer(new CachingResourceTransformer(cacheManager,
      // "default"))
            .addTransformer(new CssLinkResourceTransformer());
      // registry.addResourceHandler("/index.html").addResourceLocations("classpath:/static/index.html");
   }
}
