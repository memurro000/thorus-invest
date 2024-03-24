package com.murro.inv.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MarkPriceKline {

    private String category;
    private String symbol;
    private String[] list;
    
    
}
