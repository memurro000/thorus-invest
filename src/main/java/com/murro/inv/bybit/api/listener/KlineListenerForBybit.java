package com.murro.inv.bybit.api.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murro.inv.bybit.model.BybitKline;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class KlineListenerForBybit implements IMessageListenerForBybit{

    @NonNull
    private final ObjectMapper mapper;

    @Override
    public void onMessage(String data) {
        try {
            BybitKline kline = mapper.readValue(data, BybitKline.class);
            System.out.println(kline);
        } catch (JsonProcessingException e) {
            System.err.println("Seems not to be a proper kline: " + e.getMessage());
        }
    }
}
