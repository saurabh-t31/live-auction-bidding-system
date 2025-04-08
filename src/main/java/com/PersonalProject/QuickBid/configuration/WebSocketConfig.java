package com.PersonalProject.QuickBid.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
   
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // Clients subscribe to /topic
        /*Routes messages to the server
          When a user sends a bid, they send it to "/app/placeBid".
          The server receives it in a controller method (like @PostMapping("/placeBid")). */
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for sending messages (This is Optional)
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
/* first of all web socket Connection Is Established
1️⃣ Users subscribe to /topic/bids/{auctionId} to receive updates.
2️⃣ When a bid is placed, it is sent to /app/placeBid.
3️⃣ The server processes the bid and validates it.(Spring Boot Handles it)
4️⃣ If valid, the server broadcasts the updated price to /topic/bids/{auctionId}.
5️⃣ All subscribed users receive the update in real-time.

 */