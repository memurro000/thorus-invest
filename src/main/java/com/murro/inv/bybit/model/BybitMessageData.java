package com.murro.inv.bybit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BybitMessageData {
    private long start;
    private long end;
    private float interval;
    private float open;
    private float close;
    private float high;
    private float low;
    private float volume;
    private float turnover;
    private boolean confirm;
    private long timestamp;

    @Override
    public String toString() {
        return "BybitMessageData{" +
                "start=" + start +
                ", end=" + end +
                ", interval=" + interval +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                ", turnover=" + turnover +
                ", confirm=" + confirm +
                ", timestamp=" + timestamp +
                '}';
    }
}
