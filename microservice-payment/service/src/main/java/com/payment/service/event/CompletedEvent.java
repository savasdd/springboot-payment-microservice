package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletedEvent extends BaseEvent {
    public static final String EVENT = "ORDER_COMPLETED";
    private String orderId;

    public CompletedEvent(String orderId) {
        super(orderId);
        this.orderId = orderId;
    }
}
