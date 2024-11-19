package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductAddedEvent extends BaseEvent {
    public static final String EVENT = "PRODUCT_ITEM_ADDED";
    private String orderId;
    private String productItemId;

    public ProductAddedEvent(String orderId, String productItemId) {
        super(orderId);
        this.orderId = orderId;
        this.productItemId = productItemId;
    }
}
