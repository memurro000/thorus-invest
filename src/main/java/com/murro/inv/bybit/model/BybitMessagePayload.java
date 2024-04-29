package com.murro.inv.bybit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BybitMessagePayload {
    private String req_id;
    private String op;
    private String[] args;
}
