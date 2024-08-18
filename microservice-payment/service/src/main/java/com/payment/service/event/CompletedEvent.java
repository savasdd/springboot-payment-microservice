package com.payment.service.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletedEvent extends BaseEvent {
    private static final String ORDER_COMPLETED = "ORDER_COMPLETED";
    private String orderId;

    CompletedEvent(String orderId) {
        super(orderId);
    }
}
