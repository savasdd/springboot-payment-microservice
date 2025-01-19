package com.payment.service.event;

import com.payment.entity.model.ProductItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddedEvent extends BaseEvent {
    public static final String EVENT = "PRODUCT_ITEM_ADDED";
    private Long orderId;
    private ProductItem item;

    public AddedEvent(Long orderId, ProductItem item) {
        super(orderId);
        this.orderId = orderId;
        this.item = item;
    }
}
