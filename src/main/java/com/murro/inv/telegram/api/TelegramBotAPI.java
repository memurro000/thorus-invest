package com.murro.inv.telegram.api;

import com.murro.inv.bybit.api.listener.IMessageListenerForBybit;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;

@Component
public class TelegramBotAPI extends TelegramLongPollingBot implements IMessageListenerForBybit {

    private final ArrayList<Long> ids = new ArrayList<>();

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
        if(msg.getText().equals("/start") && !ids.contains(user.getId())){
            ids.add(user.getId());
        }
        System.out.println(ids);
    }

    public void sendStats(String symbol){

    }

    @Override
    public void onMessage(String data) {
        for(Long x : ids){
            SendMessage message = SendMessage.builder()
                    .chatId(x.toString())
                    .text(data)
                    .build();
            try{
                execute(message);
            }
            catch(TelegramApiException ex){
                System.err.println("SOMTHA WRON " + ex.getMessage());
            }
        }
    }
}
