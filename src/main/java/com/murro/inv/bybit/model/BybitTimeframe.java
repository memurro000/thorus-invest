package com.murro.inv.bybit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BybitTimeframe {
    SECOND_1("1");

    private final String title;

    @Override
    public String toString() {
        return "Timeframe{" +
                "title='" + title + '\'' +
                '}';
    }
}
