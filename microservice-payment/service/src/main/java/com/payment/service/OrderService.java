package com.payment.service;

import com.payment.common.base.BaseResponse;
import com.payment.entity.model.Order;
import com.payment.entity.model.ProductItem;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    BaseResponse createOrder(Order order);

    BaseResponse getOrder(String orderId);

    BaseResponse addProduct(ProductItem productItem);

    BaseResponse deleteProduct(String orderId, String productId);

    BaseResponse pay(String orderId, String paymentId);

    BaseResponse cancel(String orderId, String reason);

    BaseResponse submit(String orderId);

    BaseResponse complete(String orderId);

    BaseResponse getAll(Pageable pageable);

    BaseResponse deleteOutboxRecord();

}
