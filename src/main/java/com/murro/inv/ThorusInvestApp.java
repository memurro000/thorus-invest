package com.murro.inv;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import com.murro.inv.config.ThorusInvestAppConfiguration;

public class ThorusInvestApp {

    public static void main(String[] args) {
        
        ApplicationContext context = 
            new AnnotationConfigApplicationContext
                (ThorusInvestAppConfiguration.class);

        WebSocketClient stdClient = new StandardWebSocketClient();

        WebSocketHandler handler = context.getBean(MyWSHandler.class);

        WebSocketHttpHeaders connectHeaders = new WebSocketHttpHeaders();
        connectHeaders.add("Connection", "Upgrade");
        connectHeaders.add("Upgrade", "websocket");

        try{
            stdClient.execute(handler, connectHeaders, 
                    new URI("wss://stream.bybit.com/v5/public/linear"));
        }
        catch(URISyntaxException ex){
            System.err.println(ex.getMessage());
        }



        for(;;){

        }

    }

}

/*
@AllArgsConstructor
class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private final MyObjectMapper mapper;

    @Override
    public void afterConnected
        (StompSession session, StompHeaders connectedHeaders) {
        System.err.println("POBEDA");
        session.subscribe("/topic/kline.1.BTCUSDT", this);
        ObjectNode request = mapper.createObjectNode();
        request.put("args", "kline.1.BTCUSDT");


        System.out.println(session.toString());

        StompHeaders askHeaders = new StompHeaders(); 

        ArrayList<String> args = new ArrayList<>();
        args.add("kline.1.BTCUSDT");
        askHeaders.addAll("args", args);

        System.out.println(session.subscribe(askHeaders, this));
        //session.send("/app/chat", request);
    }

    @Override
    public void handleException
        (
            StompSession session, StompCommand command, 
            StompHeaders headers, byte[] payload, Throwable exception
        ) {
        System.err.println("Exception! " + exception.getMessage());
        System.err.println("on " + session + command + headers);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        throw new UnsupportedOperationException("Transport error! on " + this + session); 
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Message msg = (Message) payload;
        System.out.println("RECEIVED" + msg.getPayload().toString());
    }
    
}
*/
