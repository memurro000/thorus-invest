package com.murro.inv.bybit.api.listener;

import com.murro.inv.bybit.model.BybitMessage;
import com.murro.inv.bybit.model.BybitMessageData;
import org.springframework.stereotype.Component;

@Component
public class MessageOutputterForBybit implements IMessageListenerForBybit {
    @Override
    public void onMessage(BybitMessage message) {
        System.out.println(message);
    }
}
