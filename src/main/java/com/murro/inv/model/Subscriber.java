package com.murro.inv.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Subscriber {
    @Id
    private Long id;
    private String topic;
}
