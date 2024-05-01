package com.murro.inv.bybit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BybitMessage {

    private BybitMessageData[] data;

    private String topic;
    private long ts;
    private String type;

    @Override
    public String toString() {
        return "BybitMessage{" +
                "data=" + Arrays.toString(data) +
                ", topic='" + topic + '\'' +
                ", ts=" + ts +
                ", type='" + type + '\'' +
                '}';
    }
}
