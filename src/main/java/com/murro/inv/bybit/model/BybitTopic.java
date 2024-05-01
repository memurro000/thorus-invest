package com.murro.inv.bybit.model;


import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BybitTopic {
    KLINE ("kline");

    private final String title;

    @Override
    public String toString() {
        return title;
    }
}
