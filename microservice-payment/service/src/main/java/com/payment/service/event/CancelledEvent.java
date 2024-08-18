package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelledEvent extends BaseEvent {
    public static final String EVENT = "ORDER_CANCELLED_EVENT";
    private String orderId;
    private String description;

    public CancelledEvent(String orderId, String description) {
        super(orderId);
        this.orderId = orderId;
        this.description = description;
    }
}
