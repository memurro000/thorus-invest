package com.murro.inv.bybit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BybitMessage {

    private BybitMessageData[] data;

    private String topic;
    private int ts;
    private String type;

}
