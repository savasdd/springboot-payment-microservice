package com.payment.service.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaidEvent extends BaseEvent {
    private static final String ORDER_PAID_EVENT = "ORDER_PAID";
    private String orderId;
    private String paymentId;

    PaidEvent(String orderId, String paymentId) {
        super(orderId);
        this.paymentId = paymentId;
    }
}
