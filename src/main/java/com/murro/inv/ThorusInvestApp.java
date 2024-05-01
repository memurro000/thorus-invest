package com.murro.inv;

import com.murro.inv.bybit.api.BybitWSAPI;
import com.murro.inv.bybit.api.listener.MessageOutputterForBybit;
import com.murro.inv.bybit.model.BybitTimeframe;
import com.murro.inv.bybit.model.BybitTopic;
import com.murro.inv.config.RepoConfig;
import com.murro.inv.telegram.api.TelegramBotAPI;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Import(RepoConfig.class)
@ComponentScan(basePackages = "com.murro.inv")
public class ThorusInvestApp {

    public static void main(String[] args) {

        ApplicationContext context =
                new AnnotationConfigApplicationContext
                        (ThorusInvestApp.class);

        BybitWSAPI bybit = context.getBean(BybitWSAPI.class);
        TelegramBotAPI tgBot = context.getBean(TelegramBotAPI.class);

        TelegramBotsApi registration;
        try {
            registration = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        try {
            registration.registerBot(tgBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        tgBot.connect(bybit);
        bybit.subscribe(BybitTopic.KLINE, BybitTimeframe.SECOND_1, "BTCUSDT",
                context.getBean(MessageOutputterForBybit.class));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}