package com.techlooper.config.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * Created by phuonghqh on 12/30/14.
 */
@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

  protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
    messages
      .antMatchers("/user/**", "/queue/**").hasAuthority("USER");
//      .antMatchers("/user/**").hasAuthority("USER");

//    messages
//      .antMatchers(SimpMessageType.MESSAGE,"/queue/**","/topic/**").denyAll()
//      .antMatchers(SimpMessageType.SUBSCRIBE, "/queue/**/*-user*","/topic/**/*-user*").denyAll()
//      .antMatchers("/user/queue/errors").permitAll()
//      .anyMessage().hasRole("USER");

  }
}

