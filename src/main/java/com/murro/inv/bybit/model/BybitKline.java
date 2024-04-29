package com.murro.inv.bybit.model;

public record BybitKline(
        long start,
        long end,
        float interval,
        float open,
        float close,
        float high,
        float low,
        float volume,
        float turnover,
        boolean confirm,
        long timestamp
) implements BybitContent {
}
