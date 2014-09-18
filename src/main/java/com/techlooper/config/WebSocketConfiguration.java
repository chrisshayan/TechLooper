package com.techlooper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration extends AbstractWebSocketMessageBrokerConfigurer {

   public void registerStompEndpoints(StompEndpointRegistry registry) {
      registry.addEndpoint("/ws").withSockJS();
   }

   public void configureMessageBroker(MessageBrokerRegistry config) {
      config.enableSimpleBroker("/topic/");
      config.setApplicationDestinationPrefixes("/app");
   }
}
