package com.murro.inv.bybit.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murro.inv.bybit.api.listener.IMessageListenerForBybit;
import com.murro.inv.bybit.model.BybitMessage;
import com.murro.inv.bybit.model.BybitMessageData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jvnet.hk2.component.MultiMap;
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

    private final MultiMap<String, IMessageListenerForBybit> topicListeners = new MultiMap<>();
    
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("CONNECTA");
    }

    @Override
    public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message)
            throws Exception {
        System.out.println(message.getPayload());
        if(message.getClass().equals(PongMessage.class)){
            System.out.println("SERVER PONGS");
            return;
        }

        if(message.getClass().equals(TextMessage.class)){
            BybitMessage msg = mapper.readValue((String) message.getPayload(), BybitMessage.class);
            BybitMessageData data;
            if((data = msg.getData()[0]) != null){
                for(var x : topicListeners.get(msg.getTopic()))
                        x.onMessage(msg);
            }
            else
                System.out.println(msg);
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
        return super.supportsPartialMessages();
    }

    public void regiterListener(IMessageListenerForBybit listener, String topic) {
        topicListeners.add(topic, listener);
    }
}

