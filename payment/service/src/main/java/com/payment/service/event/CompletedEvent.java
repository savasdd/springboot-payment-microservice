package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletedEvent extends BaseEvent {
    public static final String EVENT = "ORDER_COMPLETED";
    private Long orderId;

    public CompletedEvent(Long orderId) {
        super(orderId);
        this.orderId = orderId;
    }
}
