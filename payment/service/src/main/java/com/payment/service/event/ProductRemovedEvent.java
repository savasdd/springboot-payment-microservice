package com.payment.service.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRemovedEvent extends BaseEvent {
    public static final String EVENT = "PRODUCT_ITEM_REMOVED";
    private String orderId;
    private String productItemId;

    public ProductRemovedEvent(String orderId, String productItemId) {
        super(orderId);
        this.orderId = orderId;
        this.productItemId = productItemId;
    }
}
