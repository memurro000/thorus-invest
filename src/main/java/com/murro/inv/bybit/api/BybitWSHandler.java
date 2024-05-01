package com.murro.inv.bybit.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murro.inv.bybit.api.listener.IMessageListenerForBybit;
import com.murro.inv.bybit.api.util.BybitWSAPIException;
import com.murro.inv.bybit.model.BybitMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.component.MultiMap;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public class BybitWSHandler extends AbstractWebSocketHandler {

    @NonNull
    private final ObjectMapper mapper;

    private final MultiMap<String, IMessageListenerForBybit> topicListeners = new MultiMap<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        try {
            super.handleMessage(session, message);
        } catch (Exception e) {
            throw new BybitWSAPIException(e);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        if((message.getPayload().contains("\"success\":"))){
            System.out.println(message.getPayload());
            return;
        }

        BybitMessage msg;

        try {
            msg = mapper.readValue(message.getPayload(), BybitMessage.class);
        }
        catch (JsonProcessingException e) {
            throw new BybitWSAPIException(this + " is dead because of ", e);
        }

        for(var x : topicListeners.get(msg.getTopic()))
            x.onMessage(msg);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        System.out.println(message);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) {
        System.out.println("SERVER PONGS");
    }

    @Override
    public void handleTransportError( WebSocketSession session,  Throwable exception) {
        System.err.println("Exception: " + exception.getMessage() + " occurred on session " + session);
    }

    @Override
    public void afterConnectionClosed( WebSocketSession session,  CloseStatus closeStatus) {

    }

    @Override
    public boolean supportsPartialMessages() {
        return super.supportsPartialMessages();
    }

    public void regiterListener(IMessageListenerForBybit listener, String topic) {
        topicListeners.add(topic, listener);
    }
}

