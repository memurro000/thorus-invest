package com.murro.inv.telegram.api;

import com.murro.inv.bybit.api.BybitWSAPI;
import com.murro.inv.bybit.api.listener.IMessageListenerForBybit;
import com.murro.inv.bybit.model.BybitMessage;
import com.murro.inv.bybit.model.BybitTimeframe;
import com.murro.inv.bybit.model.BybitTopic;
import com.murro.inv.telegram.api.util.TelegramBotAPIException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class TelegramBotAPI extends TelegramLongPollingBot implements IMessageListenerForBybit {

    private final HashMap<Long, String> idsSubs = new HashMap<>();
    private final ArrayList<String> activeSubscriptions = new ArrayList<>();

    private BybitWSAPI bybitAPI;

    public TelegramBotAPI() {
        this.bybitAPI = null;
    }

    @Override
    public String getBotUsername() {
        return "thorus_trading_bot";
    }

    public String getBotToken() {
        return "6879700121:AAHKk6TV0TgMGqGWCiCpRMA3CEYJ_tIp36U";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        if(msg.getText().equals("/start") && !idsSubs.containsKey(user.getId())){
            idsSubs.put(msg.getChatId(), BybitTopic.KLINE + "." + BybitTimeframe.SECOND_1 + "." + "BTCUSDT");
            subscribe(BybitTopic.KLINE, BybitTimeframe.SECOND_1, "BTCUSDT");
        }
        System.out.println(idsSubs);
    }

    public void sendStats(String symbol){

    }

    @Override
    public void onMessage(BybitMessage message) {
        System.out.println("GOTTA MESSAGE");
        for(var x : idsSubs.entrySet()
                .stream()
                .filter(
                        e -> e.getValue().equals(message.getTopic())
                )
                .toArray()
        ){
            SendMessage sendMessage =
                    SendMessage.builder()
                    .chatId(x.toString())
                    .text(message.toString())
                    .build();
            try{
                execute(sendMessage);
            }
            catch(TelegramApiException ex){
                System.err.println("SOMTHA WRON " + ex.getMessage());
            }
        }
    }

    public void connect(BybitWSAPI bybitAPI) throws TelegramBotAPIException{
        if(this.bybitAPI != null)
            throw new TelegramBotAPIException(
                    this + " is already connected to " + this.bybitAPI +
                            "\nYou still tried to connect it to " + bybitAPI
            );

        this.bybitAPI = bybitAPI;

        if(!bybitAPI.isConnected())
                bybitAPI.connect();

    }

    public void disconnectFromBybit() throws TelegramBotAPIException {
        if(bybitAPI == null)
            throw new TelegramBotAPIException(
                    this + " is not connected " +
                            "\nYou still tried to disconnect it"
            );

        try {
            bybitAPI.close();
        }
        catch (SocketException e) {
            throw new RuntimeException(
                    "Exception occurred while closing Bybit connection on " + this,
                    e
            );
        }

        bybitAPI = null;
    }

    private void subscribe(BybitTopic topic, BybitTimeframe timeframe, String symbol){
        String subscriptionDescr = topic + "." + timeframe + "." + symbol;

        if(activeSubscriptions.contains(subscriptionDescr))
            throw new TelegramBotAPIException(
                    this + " already has a subscription for " + subscriptionDescr +
                            "\nBut still was trying to open it"
            );

        bybitAPI.subscribe(topic, timeframe, symbol, this);

        activeSubscriptions.add(subscriptionDescr);
    }

    private void unsubscribe(){
        //DOES SOMTHA SOMHA
    }

}
