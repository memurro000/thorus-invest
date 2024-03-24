package com.murro.inv;

import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;

@Component
@Scope("prototype")
class MyObjectMapper extends ObjectMapper {

}

class MassageObject{
    private String op;
}

@Component
@Scope("prototype")
@AllArgsConstructor
class MyMessage implements WebSocketMessage{


    private String payload; 


    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public int getPayloadLength() {
        return payload.length();
    }

    @Override
    public boolean isLast() {
        return false;
    }

}

@Component
@Scope("prototype")
@AllArgsConstructor
public class MyWSHandler extends AbstractWebSocketHandler {

    private final ObjectMapper mapper;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.err.println("CONNECTION ESTABLISHED! \n" + session.getHandshakeHeaders());

        ObjectNode payload = mapper.createObjectNode();
        payload.put("op", "ping");
        System.out.println(mapper.writeValueAsString(payload));



        session.sendMessage(new MyMessage(mapper.writeValueAsString(payload)));
        
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.println(message.getPayload());
        
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean supportsPartialMessages() {
        // TODO Auto-generated method stub
        return false;
    }

}

