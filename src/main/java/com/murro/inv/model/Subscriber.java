package com.murro.inv.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Subscriber {
    @Id
    private Long id;
    private String topic;
}
