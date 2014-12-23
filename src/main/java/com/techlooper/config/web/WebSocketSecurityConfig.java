package com.techlooper.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * Created by phuonghqh on 12/20/14.
 */
//@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//    messages
//      .antMatchers("/user/queue/errors").permitAll()
//      .antMatchers("/**").hasRole("ADMIN");
//    messages
////      .anyMessage().permitAll()
//      .antMatchers(SimpMessageType.SUBSCRIBE, "/topic/analytics/**").hasAuthority("USER");
//      .anyMessage().permitAll();
//      .antMatchers("/user/queue/errors").permitAll()
//      .anyMessage().permitAll();
//      .anyMessage().hasRole("USER");
  }

//  protected void configure(MessageSecurityMetadataSourceRegistry messageSecurityMetadataSourceRegistry) {
//    messageSecurityMetadataSourceRegistry.pathMatcher("").
//  }
}
