package com.payment.service;

import com.payment.common.base.BaseResponse;
import com.payment.entity.dto.OrderCanselDto;
import com.payment.entity.dto.OrderDto;
import com.payment.entity.dto.ProductItemDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    BaseResponse createOrder(OrderDto order);

    BaseResponse getOrder(String orderId);

    BaseResponse addProduct(String orderId, ProductItemDto productItem);

    BaseResponse deleteProduct(String orderId, String productId);

    BaseResponse payment(String orderId, String paymentId);

    BaseResponse cancel(String orderId, OrderCanselDto dto);

    BaseResponse submit(String orderId);

    BaseResponse complete(String orderId);

    BaseResponse getAllOrder(Pageable pageable);

    void deleteOutboxRecord();

}
