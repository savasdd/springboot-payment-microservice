package com.payment.service.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelledEvent extends BaseEvent {
    private static final String ORDER_CANCELLED_EVENT = "ORDER_CANCELLED_EVENT";
    private String orderId;
    private String reason;

    CancelledEvent(String orderId, String reason) {
        super(orderId);
        this.reason = reason;
    }
}
