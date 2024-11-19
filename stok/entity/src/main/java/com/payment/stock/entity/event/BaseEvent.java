package com.payment.stock.entity.event;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEvent implements Serializable {
    private String aggregateId;
}