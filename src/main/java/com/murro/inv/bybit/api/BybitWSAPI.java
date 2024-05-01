package com.murro.inv.bybit.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murro.inv.bybit.api.listener.IMessageListenerForBybit;
import com.murro.inv.bybit.api.util.BybitWSAPIException;
import com.murro.inv.bybit.model.BybitMessagePayload;
import com.murro.inv.bybit.model.BybitTimeframe;
import com.murro.inv.bybit.model.BybitTopic;
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

    private volatile boolean isConnected = false;

    public boolean isConnected(){
        return isConnected;
    }

    public void connect() {
        if(isConnected)
            throw new BybitWSAPIException(this + " is already connected");

        try {
            session = execute(handler, "wss://stream.bybit.com/v5/public/linear").get();
        }
        catch (InterruptedException | ExecutionException e) {
            throw new BybitWSAPIException(this + " is unable to connect ", e);
        }

        isConnected = true;
    }

    public void subscribe(BybitTopic topic, BybitTimeframe timeframe, String symbol,
                          IMessageListenerForBybit subscriber
    ) {
        while (session == null) Thread.onSpinWait();

        String subscriptionDescr = topic + "." + timeframe + "." + symbol;
        BybitMessagePayload payload = new BybitMessagePayload(
                subscriptionDescr,
                "subscribe",
                new String[]{subscriptionDescr}
        );
        try {
            handler.regiterListener(subscriber, subscriptionDescr);
            session.sendMessage(new TextMessage(mapper.writeValueAsString(payload)));
        }
        catch (IOException e) {
            throw new BybitWSAPIException("Bybit API is unable to subscribe to kline ", e);
        }
    }

    public void close() throws SocketException {
        if(!isConnected && session == null)
            throw new BybitWSAPIException(this + " is already closed");

        try {
            session.close();
            session = null;
        } catch (IOException e) {
            System.err.println("Bybit API is unable to close: " + e.getMessage());
        }

        isConnected = false;
    }

}
