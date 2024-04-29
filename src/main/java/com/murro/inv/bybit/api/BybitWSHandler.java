package com.murro.inv.bybit.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murro.inv.bybit.api.listener.IMessageListenerForBybit;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.HashMap;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class BybitWSHandler extends AbstractWebSocketHandler {

    @NonNull
    private final ObjectMapper mapper;

    private final HashMap<String, IMessageListenerForBybit> topicListeners = new HashMap<>();
    
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message)
            throws Exception {
        if(message.getClass().equals(TextMessage.class)){
            JsonNode json = mapper.readTree((String) message.getPayload());
            JsonNode data;
            if((data = json.get("data")) != null){
                topicListeners
                        .get(json.get("topic").asText())
                        .onMessage(data.get(0).toString());
            }
            else
                System.out.println(json);
        }
        else if(message.getClass().equals(PongMessage.class)){
            System.out.println("SERVER PONGS");
        }
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable exception) {
        System.err.println("Exception: " + exception.getMessage() + " occurred on session " + session);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus closeStatus) throws Exception {
        
        
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void regiterListener(IMessageListenerForBybit listener, String topic) {
        topicListeners.put(topic, listener);
    }
}

