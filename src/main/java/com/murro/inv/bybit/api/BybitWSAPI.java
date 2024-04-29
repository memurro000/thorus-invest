package com.murro.inv.bybit.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murro.inv.bybit.api.listener.IMessageListenerForBybit;
import com.murro.inv.bybit.model.BybitMessagePayload;
import com.murro.inv.bybit.model.BybitTimeframe;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ExecutionException;

@Component
@Scope("prototype")
@RequiredArgsConstructor
public final class BybitWSAPI extends StandardWebSocketClient {

    @NonNull
    private final BybitWSHandler handler;
    @NonNull
    private final ObjectMapper mapper;

    @Autowired(required = false)
    private volatile WebSocketSession session;

    public void connect() throws SocketException {
        if(session != null)
            throw new SocketException("Bybit API " + this.hashCode() + " is already connected");

        try {
            session = execute(handler, "wss://stream.bybit.com/v5/public/linear").get();
        }
        catch (InterruptedException | ExecutionException e) {
            System.err.println("Bybit API is unable to connect: " + e.getMessage());
        }
    }

    public void subscribeToKline(String symbol, BybitTimeframe timeframe, IMessageListenerForBybit subscriber) {
        while (session == null) Thread.onSpinWait();

        String topic = "kline." + timeframe.getTitle() + "." + symbol;
        BybitMessagePayload payload = new BybitMessagePayload(
                topic,
                "subscribe",
                new String[]{topic}
        );
        try {
            handler.regiterListener(subscriber, topic);
            session.sendMessage(new TextMessage(mapper.writeValueAsString(payload)));
        }
        catch (IOException e) {
            throw new RuntimeException(
                    "Bybit API is unable to subscribe to kline: \n" + e.getMessage()
            );
        }
    }

    public void close() throws SocketException {
        if(session == null)
            throw new SocketException("Bybit API " + this.hashCode() + " is already closed");

        try {
            session.close();
            session = null;
        } catch (IOException e) {
            System.err.println("Bybit API is unable to close: " + e.getMessage());
        }
    }

}
