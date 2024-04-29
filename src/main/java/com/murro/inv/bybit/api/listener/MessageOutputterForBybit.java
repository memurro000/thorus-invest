package com.murro.inv.bybit.api.listener;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class MessageOutputterForBybit implements IMessageListenerForBybit {
    @Override
    public void onMessage(String data) {
        System.out.println(data);
    }
}
