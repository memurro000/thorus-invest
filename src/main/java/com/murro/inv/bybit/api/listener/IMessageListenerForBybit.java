package com.murro.inv.bybit.api.listener;

import com.murro.inv.bybit.model.BybitMessage;
import com.murro.inv.bybit.model.BybitMessageData;

import java.util.EventListener;

public interface IMessageListenerForBybit extends EventListener {
    void onMessage(BybitMessage message);
}

