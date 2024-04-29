package com.murro.inv.bybit.api.listener;

import java.util.EventListener;

public interface IMessageListenerForBybit extends EventListener {
    void onMessage(String data);
}

