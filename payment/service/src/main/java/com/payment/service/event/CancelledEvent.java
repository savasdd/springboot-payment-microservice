package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelledEvent extends BaseEvent {
    public static final String EVENT = "ORDER_CANCELLED_EVENT";
    private Long orderId;
    private String description;

    public CancelledEvent(Long orderId, String description) {
        super(orderId);
        this.orderId = orderId;
        this.description = description;
    }
}
