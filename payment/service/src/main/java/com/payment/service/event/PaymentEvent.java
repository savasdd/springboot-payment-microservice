package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentEvent extends BaseEvent {
    public static final String EVENT = "ORDER_PAID";
    private String orderId;
    private String paymentId;

    public PaymentEvent(String orderId, String paymentId) {
        super(orderId);
        this.orderId = orderId;
        this.paymentId = paymentId;
    }
}
